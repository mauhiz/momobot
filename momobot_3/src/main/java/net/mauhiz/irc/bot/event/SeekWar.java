package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.Channel;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author Topper
 * 
 */
public class SeekWar extends ChannelEvent {
    
    /**
     * liste des channels de seek
     */
    private static final String channels = "#cos_squad;#-duck-;#-hp-";
    
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(SeekWar.class);
    
    /**
     * IpPass du srv
     */
    private String ippass = "127.0.0.1:27015 pass:dtc";
    
    /**
     * level seek
     */
    private String levelSeek = "Midd";
    
    /**
     * Message de seek %P=nb Player ; %S=Serv ON/OFF ; %L = level
     */
    private String messageSeek = "seek %Pv%P - %S - %L pm ";
    
    /**
     * True si le seek est en cour ; false sinon
     */
    private boolean seekInProgress = false;
    
    /**
     * si le gather a un serv de jeu ou pass
     */
    private String servSeek = "ON";
    
    /**
     * String
     */
    private String str = "";
    
    /**
     * le temps où je commence.
     */
    private final StopWatch sw = new StopWatch();
    
    /**
     * @param channel1
     * @param commandSeek
     *            1) les param de seek :: optionel on/off (serv cs ON ou off) optionel IPPASS (si serv ON) optionel LVL
     *            optionel : msg (%P = nbPerso, %S = Serv, %L = Level)
     * 
     *            2) on "IP PASS" OU off
     * 
     */
    
    public SeekWar(final Channel channel1, final String[] commandSeek) {
        // this(channel1, commandSeek);
        super(channel1);
        sw.start();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un seek = " + commandSeek);
        }
        
        // On CFG le seek avec les param
        if (commandSeek.length == 0) {
            // Seek sans parametre
            str = "Seek Par Defaut.";
            seekInProgress = true;
            return;
            
        } else if (commandSeek.length == 2 && commandSeek[0].toLowerCase() == "on") {
            
            seekInProgress = true;
            ippass = commandSeek[1].replace("''", "");
            servSeek = "on";
            str = "Seek : serv = " + servSeek + " ippass = " + ippass;
            
        } else if (commandSeek.length == 4 && commandSeek[0].toLowerCase() == "on") {
            
            seekInProgress = true;
            servSeek = "on";
            ippass = commandSeek[1].replace("''", ""); // On supprime les ""
            levelSeek = commandSeek[2];
            messageSeek = commandSeek[3].replace("''", ""); // On supprime les ""
            str = "Seek : serv = " + servSeek + " ippass = " + ippass + " lvl = " + levelSeek + " seekMSG = "
                    + messageSeek;
            
        } else if (commandSeek.length == 1 && commandSeek[0].toLowerCase() == "off") {
            
            seekInProgress = true;
            servSeek = "off";
            str = "Seek : serv = " + servSeek;
            
        } else if (commandSeek.length == 3 && commandSeek[0].toLowerCase() == "off") {
            
            seekInProgress = true;
            servSeek = "off";
            levelSeek = commandSeek[1];
            messageSeek = commandSeek[2].replace("''", "");
            str = "Seek : serv = " + servSeek + " lvl = " + levelSeek + " seekMSG = " + messageSeek;
            
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Seek mal paramétré par l'utilisateur :: " + commandSeek);
            }
            str = "Paramètre(s) Incorrect";
        }
        
        toString();
    }
    /**
     * @return String
     */
    public final boolean isSeekInProgress() {
        return seekInProgress;
    }
    /**
     * @return seekInProgress (TRUE si le seek est déja en cour FALSE autrement)
     */
    
    public final String stopSeek() {
        seekInProgress = false;
        return "Le seek est arrete.";
    }
    /**
     * @param msg
     * @param msgChannel
     *            True si le msg est un channel, False autrement
     * @param provenace
     * @return String
     */
    
    public final String submitSeekMessage(final String msg, final boolean msgChannel, final String provenace) {
        
        return "";
    }
    /**
     * @see net.mauhiz.irc.bot.event.ChannelEvent#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        
        return str;
    }
    
    /**
     * @return un message
     */
    
}
