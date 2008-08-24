package net.mauhiz.irc.bot.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
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
public class SeekWar2 {
    /**
     * 
     */
    private static final Configuration CFG;
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Tournament.class);
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
    private final boolean isExpired;
    private String level;
    private final String[] listLevel;
    private final String message_brute;
    private final int nbPlayers;
    private final String[] seekChannels;
    private boolean serv;
    /**
     * 
     */
    private final StopWatch sw = new StopWatch();
    private Thread threadTimeOut = null;
    private final int timeOut;
    
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
    public SeekWar2(final Channel channel1, final IrcControl control1, final IrcServer ircServer1,
            final int nbPlayers1, final String param) {
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
        threadTimeOut = new SeekWarThreadTimeOut(timeOut);
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
    
    public boolean isExpired() {
        if (threadTimeOut == null) {
            return true;
        }
        return threadTimeOut.isAlive();
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
            threadTimeOut.interrupt();
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
}
