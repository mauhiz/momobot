package net.mauhiz.irc.bot.event;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.util.NetUtils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author Topper
 */
public class SeekWar2 extends ChannelEvent {
    
    // TODO : si c'est une blackliste d'utilisateur, il faut la stocker dans le même type de liste que la
    // listIrcSeekUser (par exemple)
    private static final String[] BLACKLIST;
    
    private static final String DEFAULT_IP_PW;
    private static final String DEFAULT_LVL;
    private static final boolean DEFAULT_SERV;
    
    /**
     * Pattern permettant de repérer ce qui peut ressembler à une adresse IP et un port dans une chaîne
     */
    public static final Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}\\:\\d{1,5}");
    private static final String[] LEVELS;
    /**
     * port mini
     */
    private static final int MIN_SRV_PORT = 1024;
    private static final String RAW_SEEK_MSG;
    private static final String[] SEEK_CHANNELS;
    private static final int SEEK_TIMEOUT;
    static {
        try {
            Configuration cfg = new PropertiesConfiguration("seekwar/skwar.properties");
            // On charge la cfg
            SEEK_CHANNELS = cfg.getStringArray("skwar.SEEK_CHANS");
            SEEK_TIMEOUT = cfg.getInt("skwar.TimeOut");
            DEFAULT_SERV = cfg.getBoolean("skwar.default_seek_serv");
            DEFAULT_LVL = cfg.getString("default_level");
            DEFAULT_IP_PW = cfg.getString("skwar.default_ipandpass");
            RAW_SEEK_MSG = cfg.getString("skwar.default_seekMessage");
            BLACKLIST = cfg.getStringArray("skwar.BlackList");
            LEVELS = cfg.getStringArray("skwar.default_list_level");
        } catch (ConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    /**
     * @param st
     * @return
     */
    public static boolean isMatchAgreeMessage(final String st) {
        if ((st.contains("ok") || st.contains("k") || st.contains("go") || st.contains("ac") || st.contains("oui")
                || st.contains("ui") || st.contains("yes") || st.contains("y"))
                && !isMatchUrl(st)) {
            return true;
        }
        return false;
    }
    /**
     * @param st
     * @return true si le paramètre contient une adresse ip valide, false sinon
     */
    public static boolean isMatchIp(final String st) {
        Matcher m = IP_PATTERN.matcher(st);
        if (m.find()) {
            InetSocketAddress add1 = NetUtils.makeISA(m.group());
            if (add1.getPort() > MIN_SRV_PORT) {
                return true;
            }
        }
        return false;
    }
    /**
     * Vérifie si la chaîne st est un message utilisateur indiquant le lvl de la war
     * 
     * @param st
     * @return true si il le but du message est d'indiquer le level, false sinon
     */
    public static boolean isMatchLVLMessage(final String st) {
        if ((st.contains("lvl") || st.contains("level")) && !isMatchUrl(st)) {
            return true;
        }
        return false;
    }
    /**
     * @param st
     * @return true si le paramètre st contient une URL, false sinon
     */
    public static boolean isMatchUrl(final String st) {
        if (st.contains("http://") || st.contains("www.")) {
            return true;
        }
        return false;
    }
    private final IrcChannel channel;
    
    private final IIrcControl control;
    /**
     * contient false si la war est encore d'actualité, true sinon
     */
    private boolean expired;
    private String ipPw;
    private final IrcServer ircServer;
    private String level;
    
    /**
     * Liste des utilisateurs qui sont déjà rentrés en contact avec le bot lorsqu'il était en train de seeker
     */
    private final List<SeekUserHistory> listIrcSeekUser = new ArrayList<SeekUserHistory>();
    private final int nbPlayers;
    
    private boolean serv;
    
    private final StopWatch sw = new StopWatch();
    
    private final SeekWarThreadTimeOut threadTimeOut;
    
    private SeekUserHistory winner;
    
    /**
     * 0=defaut 1=on + defaut. 2=on + level + defaut. 3=on + level + ippass. 4= off + defaut. 5= off + level.
     * 
     * @param chan
     * @param control1
     * @param ircServer1
     * @param nbPlayers1
     * 
     * @param param
     */
    public SeekWar2(final IrcChannel chan, final IIrcControl control1, final IrcServer ircServer1,
            final int nbPlayers1, final String param) {
        super(chan);
        channel = chan;
        control = control1;
        ircServer = ircServer1;
        nbPlayers = nbPlayers1;
        level = DEFAULT_LVL;
        // on cree le thread de time-out
        threadTimeOut = new SeekWarThreadTimeOut(this, SEEK_TIMEOUT);
        
        // on traite les parametres entrants
        // FIXME : Qu'est-ce que c'est que ste Regex ?
        Pattern pat = Pattern.compile("(abc)");
        Matcher match = pat.matcher(param);
        boolean b = match.matches();
        String resp = null;
        if (b) {
            switch (match.groupCount()) {
                case 0 :
                    // msg de seek par defaut
                    resp = "Lancement d'un seek = vars default";
                    LOG.debug(resp);
                    break;
                case 1 :
                    resp = "Lancement d'un seek = " + match.group();
                    LOG.debug(resp);
                    if ("on".equals(match.group().toLowerCase())) {
                        serv = true;
                    } else {
                        serv = false;
                    }
                    break;
                
                case 2 :
                    LOG.debug("Lancement d'un seek = " + match.toString());
                    level = match.group(1);
                    serv = "on".equals(match.group().toLowerCase());
                    break;
                
                case 3 :
                    resp = "Lancement d'un seek = " + match.toString();
                    LOG.debug(resp);
                    ipPw = match.group(2);
                    level = match.group(1);
                    serv = "on".equals(match.group().toLowerCase());
                    // FIXME /!\ DEBILE
                    break;
                
                default :
                    resp = "Erreur : syntaxe incorrecte.";
                    Privmsg msg = new Privmsg(null, channel.fullName(), ircServer, resp);
                    control.sendMsg(msg);
                    return;
                    
            }
        } else {
            // erreur : match !=
            resp = "Erreur : syntaxe incorrecte.";
            Privmsg msg = new Privmsg(null, channel.fullName(), ircServer, resp);
            control.sendMsg(msg);
            return;
        }
        
        // On envoie le msg
        Privmsg msg = new Privmsg(null, channel.fullName(), ircServer, resp);
        control.sendMsg(msg);
        
        // On join les #chans de seek
        for (String element : SEEK_CHANNELS) {
            Join go = new Join(ircServer, element);
            control.sendMsg(go);
        }
    }
    
    /**
     * @param user
     * @param privmsg
     */
    private void doJob(final SeekUserHistory user, final Privmsg privmsg) {
        String st;
        switch (user.getId()) {
            
            case 1 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                } else {
                    if (DEFAULT_SERV) {
                        if (isMatchLVLMessage(st)) {
                            Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                            Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "rdy?");
                            control.sendMsg(msg);
                            control.sendMsg(msg1);
                            user.setId(3);
                            break;
                        }
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "lvl?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(2);
                        break;
                    }
                    // serv off
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip pass??");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(4);
                        break;
                    }
                    Privmsg msg = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                    Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip pass?");
                    control.sendMsg(msg);
                    control.sendMsg(msg1);
                    user.setId(5);
                    break;
                }
                break;
            case 2 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                } else {
                    if (isMatchLevel(st)) {
                        user.setId(65);
                        break;
                    }
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "rdy?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(3);
                        break;
                    }
                    
                }
                break;
            case 3 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                } else {
                    if (isMatchAgreeMessage(st)) {
                        // Seek reussi on lui envois ip&pass
                        user.setId(65);
                    }
                    
                }
                break;
            case 4 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                } else {
                    // QUE FAIRE ?!?
                    break;
                }
                break;
            case 5 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                } else {
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(6);
                        break;
                    }
                }
                break;
            case 6 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                }
                break;
            case 65 :
                // seek reussi
                Privmsg msg = new Privmsg(null, user.getNick(), ircServer, DEFAULT_IP_PW);
                Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "go go!");
                control.sendMsg(msg);
                control.sendMsg(msg1);
                user.setId(100);
                break;
            case 66 :
                // Le bot PV le mec qui a envoie un msg de seek qui match
                user.add(privmsg.getTo() + " :" + privmsg.getMessage());
                if (DEFAULT_SERV) {
                    Privmsg msga = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                    Privmsg msga1 = new Privmsg(null, user.getNick(), ircServer, "lvl?");
                    control.sendMsg(msga);
                    control.sendMsg(msga1);
                    user.setId(67);
                    break;
                    
                }
                Privmsg msgb = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                Privmsg msgb1 = new Privmsg(null, user.getNick(), ircServer, "ip pass?");
                control.sendMsg(msgb);
                control.sendMsg(msgb1);
                user.setId(95);
                break;
            
            case 67 :

            case 95 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    // Seek FINI
                    user.setId(100);
                } else if (st.contains("lvl")) {
                    // Il me demande mon lvl
                    Privmsg msg0 = new Privmsg(null, user.getNick(), ircServer, level);
                    Privmsg msg01 = new Privmsg(null, user.getNick(), ircServer, "ip pass??");
                    control.sendMsg(msg0);
                    control.sendMsg(msg01);
                    user.setId(100);
                    break;
                }
                break;
            case 96 :
                st = privmsg.getMessage().toLowerCase();
                user.add(st);
                if (isMatchIp(st)) {
                    user.setId(100);
                }
                break;
            case 98 :
                // Seek Reussi
                Privmsg msgc = new Privmsg(null, user.getNick(), ircServer, "ok go");
                control.sendMsg(msgc);
                user.setId(100);
                break;
            case 100 :
                Privmsg msgd = new Privmsg(null, channel.fullName(), ircServer, "Seek reussit. " + user.getNick() + ":"
                        + privmsg.getMessage());
                control.sendMsg(msgd);
                // on leave les channels de seek
                for (String element : SEEK_CHANNELS) {
                    Part leave = new Part(ircServer, element, null);
                    control.sendMsg(leave);
                }
                winner = user;
                // On stop
                setStop("Success");
                break;
            default :
                break;
        }
    }
    
    public boolean isExpired() {
        return expired;
    }
    
    /**
     * 
     * @param privmsg
     * @return ID de l'user
     */
    private int isIncomingUserTraitment(final Privmsg privmsg) {
        return -1;
    }
    
    /**
     * Vérifie si le match décrit par le paramètre st est du même lvl que le marche recherché par l'objet SeekWar
     * 
     * @param st
     * @return true si les deux niveaux sont équivalents, false sinon.
     */
    private boolean isMatchLevel(final String st) {
        String lvl = level.toLowerCase();
        String st1 = st.toLowerCase();
        for (int i = 0; i < LEVELS.length; i++) {
            if (lvl.equals(LEVELS[i].toLowerCase())) {
                // on match le lvl dans la liste
                if (st1.equals(LEVELS[i].toLowerCase())) {
                    return true;
                }
                if (i < LEVELS.length - 1) {
                    if (st1.equals(LEVELS[i + 1].toLowerCase())) {
                        return true;
                    }
                }
                if (i > 0) {
                    if (st1.equals(LEVELS[i - 1].toLowerCase())) {
                        return true;
                    }
                }
            }
            
        }
        // Rien trouve
        return false;
    }
    
    /**
     * @param str
     * @return
     */
    private boolean isMatchMessageChannel(final String str) {
        String st = str.toLowerCase();
        if (st.contains("dispo") || st.contains("tn") || st.contains("last")) {
            return false;
        }
        Pattern patternNbJoueurs = Pattern.compile("(\\d+)\\s?(vs|o|c|x|n|on|/|v)\\s?(\\d+)");
        Matcher matcher = patternNbJoueurs.matcher(st);
        
        if (matcher.find()) {
            // on match X vs Y => on verif X=Y
            if (matcher.group(1).equals(matcher.group(3)) && matcher.group(1).equals(Integer.toString(nbPlayers))) {
                // ca match X vs X avec ce qu'on cherche
                if (isMatchLevel(st) && isMatchServer(st)) {
                    return true;
                }
            }
            
        } else {
            if (st.contains("pracc") || st.contains("pcw")) {
                // FIXME
            }
        }
        return false;
    }
    
    /**
     * @param st
     * @return
     */
    private boolean isMatchServer(final String st) {
        String st1 = st.toLowerCase();
        if (DEFAULT_SERV) {
            return true;
        }
        if (st1.contains("off")) {
            return true;
        }
        return false;
    }
    
    /**
     * Vérifie si un utilisateur est déjà présent dans la liste des utilisateurs ayant contacté le bot dans le but de
     * répondre au seek
     * 
     * @param user
     * @return true si l'utilisateur est déjà entré en contact avec le bot, false sinon
     */
    private boolean isUserAleadyIn(final SeekUserHistory user) {
        for (SeekUserHistory element : listIrcSeekUser) {
            if (element.equals(user)) {
                return true;
            }
        }
        return false;
    }
    
    private void sendSeekMessageToChannels() {
        String str;
        if (DEFAULT_SERV) {
            str = MomoStringUtils.genereSeekMessage(RAW_SEEK_MSG, nbPlayers, "on", level);
        } else {
            str = MomoStringUtils.genereSeekMessage(RAW_SEEK_MSG, nbPlayers, "off", level);
        }
        for (String element : SEEK_CHANNELS) {
            Privmsg msg = new Privmsg(null, element, ircServer, str);
            control.sendMsg(msg);
        }
    }
    
    /**
     * Annule la recherche de la war
     */
    private void setExpired() {
        threadTimeOut.cancel();
        expired = true;
    }
    
    /**
     * @param reason
     */
    public void setStop(final String reason) {
        // on leave les channels de seek
        for (String element : SEEK_CHANNELS) {
            Part leave = new Part(ircServer, element, null);
            control.sendMsg(leave);
        }
        
        // on close le thread
        setExpired();
        
        // on envoie relai le msg
        Privmsg msg = new Privmsg(null, channel.fullName(), ircServer, reason);
        control.sendMsg(msg);
    }
    
    /**
     * @param privmsg
     */
    private void submitChannelMessage(final Privmsg privmsg) {
        
        // Si le msg match => on regarde si l'user existe deja
        if (isMatchMessageChannel(privmsg.getMessage())) {
            IrcUser user1 = privmsg.getServer().findUser(new Mask(privmsg.getFrom()), true);
            SeekUserHistory user = new SeekUserHistory(user1, 66);
            if (!isUserAleadyIn(user)) {
                // On envoi le msg ID =
                listIrcSeekUser.add(user);
                doJob(user, privmsg);
            }
            
        }
        
    }
    
    /**
     * @param privmsg
     */
    public void submitIncommingMessage(final Privmsg privmsg) {
        if (privmsg == null) {
            return;
        }
        if (privmsg.isToChannel()) {
            // c'est un msg d'un channel :: On match son msg
            submitChannelMessage(privmsg);
        } else {
            // c'est un msg pv
            submitPVMessage(privmsg);
        }
    }
    
    /**
     * @param privmsg
     */
    private void submitPVMessage(final Privmsg privmsg) {
        // test l'user
        IrcUser user = privmsg.getServer().findUser(new Mask(privmsg.getFrom()), true);
        for (SeekUserHistory element : listIrcSeekUser) {
            // si il existe on forward le msg
            if (element.getNick().equals(user.getNick())) {
                doJob(element, privmsg);
                break;
            }
        }
        // sinon on cree l'user et on forward
        SeekUserHistory ircuser = new SeekUserHistory(user);
        
        // TODO : faut-il systématiquement ajouter l'utilisateur s'il PV le bot en état de seek, mais que le PV n'a rien
        // à voir avec le seek .. ?
        listIrcSeekUser.add(ircuser);
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.ChannelEvent#toString()
     */
    @Override
    public String toString() {
        String st = "Seek info - " + nbPlayers + "v" + nbPlayers + " Serv:";
        if (DEFAULT_SERV) {
            st += "on level:" + level + " ippass:" + DEFAULT_IP_PW;
        } else {
            st += "off level:" + level;
        }
        return st;
    }
}
