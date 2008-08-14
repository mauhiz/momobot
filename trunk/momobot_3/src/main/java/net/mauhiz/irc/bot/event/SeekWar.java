package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.base.data.IrcUser;

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
    public static final String channels = "#cos_squad;#-duck-;#-hp-";
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(SeekWar.class);
    /**
     * black list pour un msg pv
     */
    private String[] blackList = {"www", "http", "://", ".com", ".fr", ".eu", ".info"};
    /**
     * 
     */
    private String[] greenList = {"ok", "go", "k"};
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
     * Temps de time out
     */
    private long seekTimeOut;
    /**
     * si le gather a un serv de jeu ou pass
     */
    private String servSeek;
    /**
     * le temps où je commence.
     */
    private final StopWatch sw = new StopWatch();
    /**
     * Liste des users qui ont pv le bot
     */
    private ArrayList<IrcUser> userpv;
    /**
     * 
     */
    public SeekWar() {
        seekInProgress = false;
        servSeek = "ON";
        ippass = "127.0.0.1:27015 pass:dtc";
        levelSeek = "Midd";
        messageSeek = "seek %Pv%P - %S - %L pm ";
        seekTimeOut = 7 * 60 * 1000; // 7min
    }
    /**
     * @return String
     */
    public boolean isSeekInProgress() {
        return seekInProgress;
    }
    /**
     * @return false = TJS en vie true = DEAD!
     */
    public final boolean isTimeOut() {
        if (sw.getStartTime() < seekTimeOut) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * @param cmd
     *            String[] non normalise
     * @return un message
     */
    
    public String[] split(final String[] cmd) {
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
        sw.start();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un seek = " + StringUtils.join(commandSeek));
        }
        
        String[] cmdSeek = split(commandSeek);
        
        // On CFG le seek avec les param
        switch (cmdSeek.length) {
            case 0 : {
                // Seek sans parametre
                seekInProgress = true;
                startedSeek();
                return "Seek Par Defaut.";
            }
                
            case 1 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0 || cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    startedSeek();
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 2 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    ippass = cmdSeek[1];
                    startedSeek();
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    levelSeek = cmdSeek[1];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 3 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    ippass = cmdSeek[1];
                    levelSeek = cmdSeek[2];
                    startedSeek();
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    levelSeek = cmdSeek[1];
                    messageSeek = cmdSeek[2];
                    startedSeek();
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek
                            + " MSGSeek = " + messageSeek;
                } else {
                    sw.stop();
                    sw.reset();
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
                    startedSeek();
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + levelSeek
                            + " MSGSeek = " + messageSeek;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            default : {
                sw.stop();
                sw.reset();
                return "Paramètre(s) Incorrect";
            }
        }
    }
    private final void startedSeek() {
        // On join les channels de seek
        
        // On envoie le msg de seek 2x
        
    }
    /**
     * @return une String
     */
    public final String stopSeek() {
        seekInProgress = false;
        sw.stop();
        sw.reset();
        return "Le seek est arrete.";
    }
    
    /**
     * @param msg
     * @return true si le bot pense que le PV est ok
     */
    private final boolean submitPVMessage(final String msg) {
        
        for (String element : blackList) {
            if (msg.toUpperCase().contains(element)) {
                return false;
            }
        }
        for (String element : greenList) {
            if (msg.toUpperCase().contains(element)) {
                return true;
            }
        }
        return false;
    }
    /**
     * @param msg
     * @param provenance
     * @return String
     */
    
    public final String submitSeekMessage(final String msg, final String provenance) {
        // Traitement des messages entrant
        if (provenance.compareTo("momobot3") == 0) {
            // C'est un msg PV
            if (servSeek.toUpperCase().compareTo("on") == 0) {
                
                if (userpv.contains(provenance)) {
                    // Le bot a déja été PV par ce type
                    if (submitPVMessage(msg)) {
                        // OK le bot valide le pv
                        
                    } else {
                        // REFOULE
                        return "";
                    }
                    
                } else {
                    // Le bot est PV pour la premiere fois >> On lui demande si il est RDY
                    // userpv.add(provenance);
                    return "rdy?";
                }
                
            } else if (servSeek.toUpperCase().compareTo("off") == 0) {
                
            } else {
                // ERREUR INCONNU
                return "";
            }
            
        } else {
            // C'est un msg d'un Channel
            
        }
        
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
