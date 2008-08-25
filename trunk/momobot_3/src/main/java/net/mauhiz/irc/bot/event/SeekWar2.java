package net.mauhiz.irc.bot.event;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.NetUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.tournament.Tournament;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author Topper
 * 
 */
public class SeekWar2 extends ChannelEvent {
    /**
     * 
     */
    private static final Configuration CFG;
    private static final Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}\\:\\d{1,5}");
    private static boolean isExpired;
    
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Tournament.class);
    /**
     * port maxi
     */
    private static final int MAX_SRV_PORT = 65535;
    /**
     * port mini
     */
    private static final int MIN_SRV_PORT = 1024;
    static {
        try {
            CFG = new PropertiesConfiguration("seekar/skwar.properties");
        } catch (ConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    private final String[] blackList;
    private final Channel channel;
    private final IIrcControl control;
    private String ippass;
    private final IrcServer ircServer;
    private String level;
    private final List<IrcSeekUser> listIrcSeekUser = new ArrayList<IrcSeekUser>();
    private final String[] listLevel;
    private final String message_brute;
    private final int nbPlayers;
    private final String[] seekChannels;
    private boolean serv;
    /**
     * 
     */
    private final StopWatch sw = new StopWatch();
    private Thread threadTimeOut;
    private final int timeOut;
    private IrcSeekUser winner = null;
    
    /**
     * 0=defaut 1=on + defaut. 2=on + level + defaut. 3=on + level + ippass. 4= off + defaut. 5= off + level.
     * 
     * @param channel1
     * @param control1
     * @param ircServer1
     * @param nbPlayers1
     * 
     * @param param
     */
    public SeekWar2(final Channel channel1, final IIrcControl control1, final IrcServer ircServer1,
            final int nbPlayers1, final String param) {
        super(channel1);
        channel = channel1;
        control = control1;
        ircServer = ircServer1;
        nbPlayers = nbPlayers1;
        
        // On charge la cfg
        seekChannels = CFG.getStringArray("skwar.SEEK_CHANS");
        timeOut = CFG.getInt("skwar.TimeOut");
        serv = CFG.getBoolean("skwar.default_seek_serv");
        level = CFG.getString("default_level");
        ippass = CFG.getString("skwar.default_ipandpass");
        message_brute = CFG.getString("skwar.default_seekMessage");
        blackList = CFG.getStringArray("skwar.BlackList");
        listLevel = CFG.getStringArray("skwar.default_list_level");
        
        // on crée le thread de time-out
        threadTimeOut = new SeekWarThreadTimeOut(this, timeOut);
        threadTimeOut.start();
        
        // on traite les parametres entrants
        Pattern pat = Pattern.compile("(abc)");
        Matcher match = pat.matcher(param);
        boolean b = match.matches();
        String resp = null;
        if (b) {
            switch (match.groupCount()) {
                case 0 : {
                    // msg de seek par defaut
                    resp = "Lancement d'un seek = vars default";
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(resp);
                    }
                    break;
                }
                case 1 : {
                    resp = "Lancement d'un seek = " + match.group();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(resp);
                    }
                    if ("on".equals(match.group().toLowerCase())) {
                        serv = true;
                    } else {
                        serv = false;
                    }
                    break;
                }
                case 2 : {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Lancement d'un seek = " + match.toString());
                    }
                    level = match.group(1);
                    if ("on".equals(match.group().toLowerCase())) {
                        serv = true;
                    } else {
                        serv = false;
                    }
                    break;
                }
                case 3 : {
                    resp = "Lancement d'un seek = " + match.toString();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(resp);
                    }
                    ippass = match.group(2);
                    level = match.group(1);
                    if ("on".equals(match.group().toLowerCase())) {
                        serv = true;
                    } else {
                        serv = false;
                        // ERREUR :: /!\ DEBILE
                    }
                    break;
                }
                default : {
                    resp = "Erreur : synthaxe incorrecte.";
                    Privmsg msg = new Privmsg(null, channel.toString(), ircServer, resp);
                    control.sendMsg(msg);
                    return;
                }
            }
        } else {
            // erreur : match !=
            resp = "Erreur : synthaxe incorrecte.";
            Privmsg msg = new Privmsg(null, channel.toString(), ircServer, resp);
            control.sendMsg(msg);
            return;
        }
        
        // On envoie le msg
        Privmsg msg = new Privmsg(null, channel.toString(), ircServer, resp);
        control.sendMsg(msg);
        
        // On join les #chans de seek
        for (String element : seekChannels) {
            Join go = new Join(ircServer, element);
            control.sendMsg(go);
        }
        return;
    }
    private void doJob(final IrcSeekUser user, final Privmsg privmsg) {
        
        switch (user.getID()) {
            
            case 1 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                } else {
                    if (serv) {
                        if (isMatchLVLMessage(st)) {
                            Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                            Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "rdy?");
                            control.sendMsg(msg);
                            control.sendMsg(msg1);
                            user.setID(3);
                            break;
                        }
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "lvl?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setID(2);
                        break;
                    }
                    // serv off
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip pass??");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setID(4);
                        break;
                    }
                    Privmsg msg = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                    Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip pass?");
                    control.sendMsg(msg);
                    control.sendMsg(msg1);
                    user.setID(5);
                    break;
                }
            }
                
            case 2 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                } else {
                    if (isMatchLevel(st)) {
                        user.setID(65);
                        break;
                    }
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "rdy?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setID(3);
                        break;
                    }
                    
                }
            }
                
            case 3 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                } else {
                    if (isMatchAgreeMessage(st)) {
                        // Seek reussi on lui envois ip&pass
                        user.setID(65);
                    }
                    
                }
                
            }
                
            case 4 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                } else {
                    // QUE FAIRE ?!?
                    break;
                }
            }
                
            case 5 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                } else {
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                        Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setID(6);
                        break;
                    }
                }
            }
                
            case 6 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                }
            }
                
            case 65 : {
                // seek reussi
                Privmsg msg = new Privmsg(null, user.getNick(), ircServer, ippass);
                Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "go go!");
                control.sendMsg(msg);
                control.sendMsg(msg1);
                user.setID(100);
            }
                
            case 66 : {
                // Le bot PV le mec qui a envoie un msg de seek qui match
                user.addStringToHistory(privmsg.getTo().toString() + " :" + privmsg.getMessage());
                if (serv) {
                    Privmsg msg = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                    Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "lvl?");
                    control.sendMsg(msg);
                    control.sendMsg(msg1);
                    user.setID(67);
                    break;
                    
                }
                Privmsg msg = new Privmsg(null, user.getNick(), ircServer, user.getNick());
                Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip pass?");
                control.sendMsg(msg);
                control.sendMsg(msg1);
                user.setID(95);
                break;
            }
                
            case 67 : {
                
            }
                
            case 95 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    // Seek FINI
                    user.setID(100);
                } else if (st.contains("lvl")) {
                    // Il me demande mon lvl
                    Privmsg msg = new Privmsg(null, user.getNick(), ircServer, level);
                    Privmsg msg1 = new Privmsg(null, user.getNick(), ircServer, "ip pass??");
                    control.sendMsg(msg);
                    control.sendMsg(msg1);
                    user.setID(100);
                    break;
                }
            }
                
            case 96 : {
                String st = privmsg.getMessage().toLowerCase();
                user.addStringToHistory(st);
                if (isMatchIp(st)) {
                    user.setID(100);
                }
            }
                
            case 98 : {
                // Seek Reussi
                Privmsg msg = new Privmsg(null, user.getNick(), ircServer, "ok go");
                control.sendMsg(msg);
                user.setID(100);
                
            }
                
            case 100 : {
                Privmsg msg = new Privmsg(null, channel.toString(), ircServer, "Seek reussit. " + user.getNick() + ":"
                        + privmsg.getMessage());
                control.sendMsg(msg);
                // on leave les channels de seek
                for (String element : seekChannels) {
                    Part leave = new Part(ircServer, element, null);
                    control.sendMsg(leave);
                }
                winner = user;
                // On stop
                setStop("Success");
            }
        }
    }
    public boolean isExpired() {
        return threadTimeOut.isAlive();
    }
    
    /**
     * 
     * @param privmsg
     * @return ID de l'user
     */
    private int isIncommingUserTraitment(final Privmsg privmsg) {
        return -1;
    }
    
    private boolean isMatchAgreeMessage(final String st) {
        if ((st.contains("ok") || st.contains("k") || st.contains("go") || st.contains("ac") || st.contains("oui")
                || st.contains("ui") || st.contains("yes") || st.contains("y"))
                && !isMatchUrl(st)) {
            return true;
        }
        return false;
    }
    
    private boolean isMatchIp(final String st) {
        Matcher m = IP_PATTERN.matcher(st);
        if (m.find()) {
            InetSocketAddress add1 = NetUtils.makeISA(m.group());
            if (add1.getPort() > MIN_SRV_PORT && add1.getPort() < MAX_SRV_PORT) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isMatchLevel(final String st) {
        String lvl = level.toLowerCase();
        String st1 = st.toLowerCase();
        for (int i = 0; i < listLevel.length; i++) {
            if (lvl.equals(listLevel[i].toLowerCase())) {
                // on match le lvl dans la liste
                if (st1.equals(listLevel[i].toLowerCase())) {
                    return true;
                }
                if (i < listLevel.length - 1) {
                    if (st1.equals(listLevel[i + 1].toLowerCase())) {
                        return true;
                    }
                }
                if (i > 0) {
                    if (st1.equals(listLevel[i - 1].toLowerCase())) {
                        return true;
                    }
                }
            }
            
        }
        // Rien trouvé
        return false;
    }
    
    private boolean isMatchLVLMessage(final String st) {
        if ((st.contains("lvl") || st.contains("level")) && !isMatchUrl(st)) {
            return true;
        }
        return false;
    }
    
    private boolean isMatchMessageChannel(final String str) {
        String st = str.toLowerCase();
        if (st.contains("dispo") || st.contains("tn") || st.contains("last")) {
            return false;
        }
        Pattern patternNbJoueurs = Pattern.compile("(\\d+)\\s?(vs|o|c|x|n|on|/|v)\\s?(\\d+)");
        Matcher matcher = patternNbJoueurs.matcher(st);
        
        if (matcher.find()) {
            // on match X vs Y => on verif X=Y
            if (matcher.group(1).compareTo(matcher.group(3)) == 0 && matcher.group(1).compareTo(nbPlayers + "") == 0) {
                // ça match X vs X avec ce qu'on cherche
                if (isMatchLevel(st)) {
                    if (isMatchServer(st)) {
                        return true;
                    }
                }
            }
            
        } else {
            if (st.contains("pracc") || st.contains("pcw")) {
                
            }
        }
        return false;
    }
    
    private boolean isMatchServer(final String st) {
        String st1 = st.toLowerCase();
        if (serv) {
            return true;
        }
        if (st1.contains("off")) {
            return true;
        }
        return false;
    }
    
    private boolean isMatchUrl(final String st) {
        if (st.contains("http://") || st.contains("www.")) {
            return true;
        }
        return false;
    }
    private boolean isUserAleadyIn(final IrcSeekUser user) {
        for (IrcSeekUser element : listIrcSeekUser) {
            if (element.equals(user)) {
                return true;
            }
        }
        return false;
    }
    private void sendSeekMessageToChannels() {
        String str;
        if (serv) {
            str = MomoStringUtils.genereSeekMessage(message_brute, nbPlayers, "on", level);
        } else {
            str = MomoStringUtils.genereSeekMessage(message_brute, nbPlayers, "off", level);
        }
        for (String element : seekChannels) {
            Privmsg msg = new Privmsg(null, element, ircServer, str);
            control.sendMsg(msg);
        }
    }
    
    private void setExpired() {
        if (threadTimeOut.isAlive()) {
            // crado
            SeekWarSelecter.isRunnable = false;
        }
    }
    
    public void setStop(final String reason) {
        // on leave les channels de seek
        for (String element : seekChannels) {
            Part leave = new Part(ircServer, element, null);
            control.sendMsg(leave);
        }
        
        // on close le thread
        setExpired();
        
        // on envoie relai le msg
        Privmsg msg = new Privmsg(null, channel.toString(), ircServer, reason);
        control.sendMsg(msg);
    }
    
    private void submitChannelMessage(final Privmsg privmsg) {
        
        // Si le msg match => on regarde si l'user existe deja
        if (isMatchMessageChannel(privmsg.getMessage())) {
            IrcUser user1 = Users.getInstance(privmsg.getServer()).findUser(new Mask(privmsg.getFrom()), true);
            IrcSeekUser user = new IrcSeekUser(user1.getNick(), 66);
            if (!isUserAleadyIn(user)) {
                // On envoi le msg ID =
                listIrcSeekUser.add(user);
                doJob(user, privmsg);
            }
            
        }
        
    }
    
    public void submitIncommingMessage(final Privmsg privmsg) {
        if (privmsg == null) {
            return;
        }
        if (Channels.isChannelName(privmsg.getFrom())) {
            // c'est un msg d'un channel :: On match son msg
            submitChannelMessage(privmsg);
        } else {
            // c'est un msg pv
            submitPVMessage(privmsg);
        }
    }
    
    private void submitPVMessage(final Privmsg privmsg) {
        // test l'user
        IrcUser user = Users.getInstance(privmsg.getServer()).findUser(new Mask(privmsg.getFrom()), true);
        for (IrcSeekUser element : listIrcSeekUser) {
            // si il existe on forward le msg
            if (element.equals(user)) {
                doJob(element, privmsg);
                break;
            }
        }
        // sinon on crée l'user et on forward
        IrcSeekUser ircuser = new IrcSeekUser(user.getNick(), 1);
        listIrcSeekUser.add(ircuser);
    }
    
    @Override
    public final String toString() {
        String st = "Seek info - " + nbPlayers + "v" + nbPlayers + " Serv:";
        if (serv) {
            st += "on level:" + level + " ippass:" + ippass;
        } else {
            st += "off level:" + level;
        }
        return st;
    }
}
