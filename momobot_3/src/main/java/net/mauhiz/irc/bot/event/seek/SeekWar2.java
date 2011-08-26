package net.mauhiz.irc.bot.event.seek;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.ArgumentList;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.AbstractChannelEvent;
import net.mauhiz.util.NetUtils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

/**
 * @author Topper
 */
public class SeekWar2 extends AbstractChannelEvent {

    // TODO : si c'est une blackliste d'utilisateur, il faut la stocker dans le meme type de liste que la
    // listIrcSeekUser (par exemple)
    private static final String[] BLACKLIST;

    private static final String DEFAULT_IP_PW;
    private static final String DEFAULT_LVL;
    private static final boolean DEFAULT_SERV;

    /**
     * Pattern permettant de rep�rer ce qui peut ressembler � une adresse IP et un port dans une cha�ne
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
        } catch (ConfigurationException ce) {
            throw new ExceptionInInitializerError(ce);
        }
    }

    /**
     * @param st
     * @return
     */
    public static boolean isMatchAgreeMessage(String st) {
        if (isMatchUrl(st)) {
            return false;
        }

        String[] okStrs = { "ok", "k", "go", "ac", "oui", "ui", "yes", "y" };
        for (String okStr : okStrs) {
            if (StringUtils.contains(st, okStr)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param st
     * @return true si le param�tre contient une adresse ip valide, false sinon
     */
    public static boolean isMatchIp(final String st) {
        Matcher m = IP_PATTERN.matcher(st);
        if (m.find()) {
            try {
                SocketAddress add1 = NetUtils.makeISA(m.group());
                return NetUtils.checkPortRange(add1, MIN_SRV_PORT, Character.MAX_VALUE);
            } catch (IllegalArgumentException iae) {
                return false;
            }
        }
        return false;
    }

    /**
     * V�rifie si la cha�ne st est un message utilisateur indiquant le lvl de la war
     * 
     * @param st
     * @return true si il le but du message est d'indiquer le level, false sinon
     */
    public static boolean isMatchLVLMessage(final String st) {
        if (isMatchUrl(st)) {
            return false;
        }
        if (st.contains("lvl") || st.contains("level")) {
            return true;
        }
        return false;
    }

    /**
     * @param st
     * @return
     */
    private static boolean isMatchServer(final String st) {
        String st1 = st.toLowerCase(Locale.FRENCH);
        if (DEFAULT_SERV) {
            return true;
        }
        if (st1.contains("off")) {
            return true;
        }
        return false;
    }

    /**
     * @param st
     * @return true si le param�tre st contient une URL, false sinon
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
     * contient false si la war est encore d'actualit�, true sinon
     */
    private boolean expired;
    private String ipPw;
    private final IIrcServerPeer ircServer;

    private String level;
    /**
     * Liste des utilisateurs qui sont d�j� rentr�s en contact avec le bot lorsqu'il �tait en train de seeker
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
     * @param args
     */
    public SeekWar2(final IrcChannel chan, final IIrcControl control1, final IIrcServerPeer ircServer1,
            final int nbPlayers1, final ArgumentList args) {
        super(chan);
        channel = chan;
        control = control1;
        ircServer = ircServer1;
        nbPlayers = nbPlayers1;
        level = DEFAULT_LVL;
        // on cree le thread de time-out
        threadTimeOut = new SeekWarThreadTimeOut(this, SEEK_TIMEOUT);

        // on traite les parametres entrants
        String resp;
        if (args.isEmpty()) {

            // msg de seek par defaut
            resp = "Lancement d'un seek = vars default";
        } else {

            String server = args.poll();
            serv = "on".equals(server.toLowerCase(Locale.FRENCH));
            level = args.poll();
            ipPw = args.poll();

            resp = "Lancement d'un seek = " + args.getMessage();
        }

        // On envoie le msg
        Privmsg msg = new Privmsg(ircServer, null, channel, resp);
        control.sendMsg(msg);

        // On join les #chans de seek
        for (String element : SEEK_CHANNELS) {
            Join go = new Join(ircServer, ircServer.getNetwork().findChannel(element));
            control.sendMsg(go);
        }
    }

    /**
     * @param user
     * @param privmsg
     */
    private void doJob(final SeekUserHistory user, final Privmsg privmsg) {
        final String st = privmsg.getMessage().toLowerCase(Locale.FRENCH);
        user.add(st);
        switch (user.getId()) {

            case STARTING:
                if (isMatchIp(st)) {
                    user.setId(SeekStatus.GOT_IP);
                } else {
                    if (DEFAULT_SERV) {
                        if (isMatchLVLMessage(st)) {
                            Privmsg msg = new Privmsg(ircServer, null, user.getUser(), level);
                            Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "rdy?");
                            control.sendMsg(msg);
                            control.sendMsg(msg1);
                            user.setId(SeekStatus.GOT_MATCH_LVL);
                            break;
                        }
                        Privmsg msg = new Privmsg(ircServer, null, user.getUser(), user.getNick());
                        Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "lvl?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(SeekStatus.GOT_ANSWER);
                        break;
                    }
                    // serv off
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(ircServer, null, user.getUser(), level);
                        Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "ip pass??");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(SeekStatus.GOT_MATCH_LVL_SRV_OFF);
                        break;
                    }
                    Privmsg msg = new Privmsg(ircServer, null, user.getUser(), user.getNick());
                    Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "ip pass?");
                    control.sendMsg(msg);
                    control.sendMsg(msg1);
                    user.setId(SeekStatus.GOT_ANSWER_SRV_OFF);
                    break;
                }
                break;
            case GOT_ANSWER:
                if (isMatchIp(st)) {
                    user.setId(SeekStatus.GOT_IP);
                } else {
                    if (isMatchLevel(st)) {
                        user.setId(SeekStatus.GOT_MATCH_LVL_ANSWER);
                        break;
                    }
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(ircServer, null, user.getUser(), level);
                        Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "rdy?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(SeekStatus.GOT_MATCH_LVL);
                        break;
                    }

                }
                break;
            case GOT_MATCH_LVL:
                if (isMatchIp(st)) {
                    user.setId(SeekStatus.GOT_IP);
                } else {
                    if (isMatchAgreeMessage(st)) {
                        // Seek reussi on lui envois ip&pass
                        user.setId(SeekStatus.GOT_MATCH_LVL_ANSWER);
                    }

                }
                break;
            case GOT_ANSWER_SRV_OFF:
                if (isMatchIp(st)) {
                    user.setId(SeekStatus.GOT_IP);
                } else {
                    if (isMatchLVLMessage(st)) {
                        Privmsg msg = new Privmsg(ircServer, null, user.getUser(), level);
                        Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "ip?");
                        control.sendMsg(msg);
                        control.sendMsg(msg1);
                        user.setId(SeekStatus.ASKED_IP);
                        break;
                    }
                }
                break;
            case UNK_96:
            case GOT_MATCH_LVL_SRV_OFF:
            case ASKED_IP:
                if (isMatchIp(st)) {
                    user.setId(SeekStatus.GOT_IP);
                }
                break;
            case GOT_MATCH_LVL_ANSWER:
                // seek reussi
                Privmsg msg = new Privmsg(ircServer, null, user.getUser(), DEFAULT_IP_PW);
                Privmsg msg1 = new Privmsg(ircServer, null, user.getUser(), "go go!");
                control.sendMsg(msg);
                control.sendMsg(msg1);
                user.setId(SeekStatus.GOT_IP);
                break;
            case SENT_MSG:
                // Le bot PV le mec qui a envoie un msg de seek qui match
                user.add(privmsg.getTo() + " :" + privmsg.getMessage());
                if (DEFAULT_SERV) {
                    Privmsg msga = new Privmsg(ircServer, null, user.getUser(), user.getNick());
                    Privmsg msga1 = new Privmsg(ircServer, null, user.getUser(), "lvl?");
                    control.sendMsg(msga);
                    control.sendMsg(msga1);
                    user.setId(SeekStatus.ASKED_MATCH_LVL);
                    break;

                }
                Privmsg msgb = new Privmsg(ircServer, null, user.getUser(), user.getNick());
                Privmsg msgb1 = new Privmsg(ircServer, null, user.getUser(), "ip pass?");
                control.sendMsg(msgb);
                control.sendMsg(msgb1);
                user.setId(SeekStatus.ASKED_IP2);
                break;

            case ASKED_MATCH_LVL:

            case ASKED_IP2:
                if (isMatchIp(st)) {
                    // Seek FINI
                    user.setId(SeekStatus.GOT_IP);
                } else if (st.contains("lvl")) {
                    // Il me demande mon lvl
                    Privmsg msg0 = new Privmsg(ircServer, null, user.getUser(), level);
                    Privmsg msg01 = new Privmsg(ircServer, null, user.getUser(), "ip pass??");
                    control.sendMsg(msg0);
                    control.sendMsg(msg01);
                    user.setId(SeekStatus.GOT_IP);
                    break;
                }
                break;
            case UNK_98:
                // Seek Reussi
                Privmsg msgc = new Privmsg(ircServer, null, user.getUser(), "ok go");
                control.sendMsg(msgc);
                user.setId(SeekStatus.GOT_IP);
                break;
            case GOT_IP:
                Privmsg msgd = new Privmsg(ircServer, null, channel, "Seek reussi. " + user.getNick() + ":"
                        + privmsg.getMessage());
                control.sendMsg(msgd);
                // on leave les channels de seek
                for (String element : SEEK_CHANNELS) {
                    Part leave = new Part(ircServer, ircServer.getNetwork().findChannel(element));
                    control.sendMsg(leave);
                }
                winner = user;
                // On stop
                setStop("Success");
                break;
            default:
                break;
        }
    }

    public SeekUserHistory getWinner() {
        return winner;
    }

    public boolean isExpired() {
        return expired;
    }

    /**
     * 
     * @param privmsg
     * @return ID de l'user
     */
    int isIncomingUserTraitment(final Privmsg privmsg) {
        return -1;
    }

    /**
     * V�rifie si le match d�crit par le param�tre st est du m�me lvl que le marche recherch� par l'objet SeekWar
     * 
     * @param st
     * @return true si les deux niveaux sont �quivalents, false sinon.
     */
    private boolean isMatchLevel(final String st) {
        String lvl = level.toLowerCase(Locale.FRENCH);
        String st1 = st.toLowerCase(Locale.FRENCH);
        for (int i = 0; i < LEVELS.length; i++) {
            if (lvl.equals(LEVELS[i].toLowerCase(Locale.FRENCH))) {
                // on match le lvl dans la liste
                if (st1.equals(LEVELS[i].toLowerCase(Locale.FRENCH))) {
                    return true;
                }
                if (i < LEVELS.length - 1) {
                    if (st1.equals(LEVELS[i + 1].toLowerCase(Locale.FRENCH))) {
                        return true;
                    }
                }
                if (i > 0) {
                    if (st1.equals(LEVELS[i - 1].toLowerCase(Locale.FRENCH))) {
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
        String st = str.toLowerCase(Locale.FRENCH);
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
                LOG.debug("Not found but...");
            }
        }
        return false;
    }

    /**
     * V�rifie si un utilisateur est d�j� pr�sent dans la liste des utilisateurs ayant contact� le bot dans le but de
     * r�pondre au seek
     * 
     * @param user
     * @return true si l'utilisateur est d�j� entr� en contact avec le bot, false sinon
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
            Privmsg msg = new Privmsg(ircServer, null, ircServer.getNetwork().findChannel(element), str);
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
            Part leave = new Part(ircServer, ircServer.getNetwork().findChannel(element), null);
            control.sendMsg(leave);
        }

        // on close le thread
        setExpired();

        // on envoie relai le msg
        Privmsg msg = new Privmsg(ircServer, null, channel, reason);
        control.sendMsg(msg);
    }

    /**
     * @param privmsg
     */
    private void submitChannelMessage(final Privmsg privmsg) {

        // Si le msg match => on regarde si l'user existe deja
        if (isMatchMessageChannel(privmsg.getMessage())) {
            IrcUser user1 = (IrcUser) privmsg.getFrom();
            SeekUserHistory user = new SeekUserHistory(user1, SeekStatus.SENT_MSG);
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
        IrcUser user = (IrcUser) privmsg.getFrom();
        for (SeekUserHistory element : listIrcSeekUser) {
            // si il existe on forward le msg
            if (element.getNick().equals(user.getNick())) {
                doJob(element, privmsg);
                break;
            }
        }
        // sinon on cree l'user et on forward
        SeekUserHistory ircuser = new SeekUserHistory(user);

        // TODO : faut-il systematiquement ajouter l'utilisateur s'il PV le bot en etat de seek, mais que le PV n'a rien
        // a voir avec le seek .. ?
        listIrcSeekUser.add(ircuser);
    }

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
