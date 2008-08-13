package net.mauhiz.irc.bot.event;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author Topper
 * 
 */
public class SeekWar {
    
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
    private String ippass;
    /**
     * level seek
     */
    private String levelSeek;
    /**
     * Message de seek %P=nb Player ; %S=Serv ON/OFF ; %L = level
     */
    private String messageSeek;
    /**
     * True si le seek est en cour ; false sinon
     */
    private boolean seekInProgress;
    /**
     * si le gather a un serv de jeu ou pass
     */
    private String servSeek;
    /**
     * String
     */
    private String str = "";
    /**
     * le temps où je commence.
     */
    private final StopWatch sw1 = new StopWatch();
    
    /**
     * @param channel1
     * 
     */
    
    public SeekWar() {
        seekInProgress = false;
        servSeek = "ON";
        ippass = "127.0.0.1:27015 pass:dtc";
        levelSeek = "Midd";
        messageSeek = "seek %Pv%P - %S - %L pm ";
    }
    
    /**
     * @return String
     */
    public boolean isSeekInProgress() {
        return seekInProgress;
    }
    /**
     * @param commandSeek
     * @return String
     */
    public String start(final String[] commandSeek) {
        sw1.start();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un seek = " + commandSeek);
        }
        
        // On CFG le seek avec les param
        if (commandSeek.length == 0) {
            // Seek sans parametre
            seekInProgress = true;
            return "Seek Par Defaut.";
            
        } else if (commandSeek.length == 2 && commandSeek[0].toLowerCase() == "on") {
            
            seekInProgress = true;
            ippass = commandSeek[1].replace("''", "");
            servSeek = "on";
            return "Seek : serv = " + servSeek + " ippass = " + ippass;
            
        } else if (commandSeek.length == 4 && commandSeek[0].toLowerCase() == "on") {
            
            seekInProgress = true;
            servSeek = "on";
            ippass = commandSeek[1].replace("''", ""); // On supprime les ""
            levelSeek = commandSeek[2];
            messageSeek = commandSeek[3].replace("''", ""); // On supprime les ""
            return "Seek : serv = " + servSeek + " ippass = " + ippass + " lvl = " + levelSeek + " seekMSG = "
                    + messageSeek;
            
        } else if (commandSeek.length == 1 && commandSeek[0].toLowerCase() == "off") {
            
            seekInProgress = true;
            servSeek = "off";
            return "Seek : serv = " + servSeek;
            
        } else if (commandSeek.length == 3 && commandSeek[0].toLowerCase() == "off") {
            
            seekInProgress = true;
            servSeek = "off";
            levelSeek = commandSeek[1];
            messageSeek = commandSeek[2].replace("''", "");
            return "Seek : serv = " + servSeek + " lvl = " + levelSeek + " seekMSG = " + messageSeek;
            
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Seek mal paramétré par l'utilisateur :: " + commandSeek);
            }
            return "Paramètre(s) Incorrect";
        }
        
    }
    /**
     * @return seekInProgress (TRUE si le seek est déja en cour FALSE autrement)
     */
    
    public final String stopSeek() {
        seekInProgress = false;
        sw1.stop();
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
        
        return "";
    }
    
    /**
     * @return un message
     */
    
}
