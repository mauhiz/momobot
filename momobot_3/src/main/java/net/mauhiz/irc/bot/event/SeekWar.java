package net.mauhiz.irc.bot.event;

import org.apache.commons.lang.StringUtils;
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
     * @param cmd
     *            String[] non normalise
     * @return un message
     */
    
    public String[] Split(final String[] cmd) {
        String[] cmdNormalise = new String[0];
        String tmpStg = "";
        
        boolean Inquote = false;
        for (String element : cmd) {
            
            if (element.charAt(0) == '\"' || element.charAt(element.length() - 1) == '\"') {
                Inquote = !Inquote;
            }
            
            if (tmpStg.isEmpty()) {
                tmpStg = element;
            } else {
                tmpStg += " " + element;
            }
            if (!Inquote) {
                
                String[] tmp = new String[cmdNormalise.length + 1];
                for (int i = 0; i < cmdNormalise.length; i++) {
                    tmp[i] = cmdNormalise[i];
                }
                tmp[cmdNormalise.length] = tmpStg;
                cmdNormalise = tmp;
                tmpStg = "";
            }
            
        }
        
        return cmdNormalise;
    }
    
    /**
     * @param commandSeek
     *            1) RIEN 2) ON IPPASS LVL 3) OFF LVL 4) ON IPPASS LVL MSGSEEK 5) OFF LVL MSGSEEK
     * @return String
     */
    public String start(final String[] commandSeek) {
        sw1.start();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un seek = " + StringUtils.join(commandSeek));
        }
        
        String[] cmdSeek = Split(commandSeek);
        
        // On CFG le seek avec les param
        switch (cmdSeek.length) {
            case 0 : {
                // Seek sans parametre
                seekInProgress = true;
                return "Seek Par Defaut.";
            }
                
            case 1 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0 || cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else {
                    sw1.stop();
                    sw1.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 2 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    ippass = cmdSeek[1];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    levelSeek = cmdSeek[1];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else {
                    sw1.stop();
                    sw1.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 3 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    ippass = cmdSeek[1];
                    levelSeek = cmdSeek[2];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    levelSeek = cmdSeek[1];
                    messageSeek = cmdSeek[2];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek
                            + " MSGSeek = " + messageSeek;
                } else {
                    sw1.stop();
                    sw1.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 4 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    ippass = cmdSeek[1];
                    levelSeek = cmdSeek[2];
                    messageSeek = cmdSeek[3];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek
                            + " MSGSeek = " + messageSeek;
                } else {
                    sw1.stop();
                    sw1.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            default : {
                sw1.stop();
                sw1.reset();
                return "Paramètre(s) Incorrect";
            }
        }
        
    }
    /**
     * @return seekInProgress (TRUE si le seek est déja en cour FALSE autrement)
     */
    
    public final String stopSeek() {
        seekInProgress = false;
        sw1.stop();
        sw1.reset();
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
    
}
