package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

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
    private String[] greenList = {"ok", "oui", "go", "k", "ip", "pass"};
    /**
     * IpPass du srv
     */
    private String ippass;
    /**
     * Message de seek %P=nb Player ; %S=Serv ON/OFF ; %L = level
     */
    private String messageSeek;
    /**
     * True si le seek est en cour ; false sinon
     */
    private boolean seekInProgress;
    /**
     * level seek
     */
    private String seekLevel;
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
    private ArrayList<String> userpv;
    /**
     * 
     */
    public SeekWar() {
        seekInProgress = false;
        servSeek = "ON";
        ippass = "127.0.0.1:27015 pass:dtc";
        seekLevel = "Midd";
        messageSeek = "seek %Pv%P - %S - %L pm ";
        seekTimeOut = 7 * 60 * 10000; // 7min
    }
    /**
     * 
     */
    public final String getSeekLevel() {
        return seekLevel;
    }
    /**
     * 
     */
    public final String getSeekMessage() {
        return messageSeek;
    }
    /**
     * 
     */
    public final String getSeekServ() {
        return servSeek;
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
        }
        return true;
    }
    
    /**
     * Rajoute l'élément au tableau
     * 
     * @param tbl
     * @param element
     * @return
     */
    private final String[] rajouteElement(final String[] tbl, final String element) {
        String[] resultTbl = new String[tbl.length + 1];
        for (int i = 0; i < tbl.length; i++) {
            resultTbl[i] = tbl[i];
        }
        resultTbl[tbl.length] = element;
        return resultTbl;
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
                return "Seek Par Defaut.";
            }
                
            case 1 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0 || cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + seekLevel;
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
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    seekLevel = cmdSeek[1];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + seekLevel;
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
                    seekLevel = cmdSeek[2];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    servSeek = cmdSeek[0];
                    seekLevel = cmdSeek[1];
                    messageSeek = cmdSeek[2];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + seekLevel
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
                    seekLevel = cmdSeek[2];
                    messageSeek = cmdSeek[3];
                    return "Seek : serv = " + servSeek + " ippass = " + ippass + " level = " + seekLevel
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
        
        // RAJOUTE UNE DETECTION D'UNE IP&PASS
        
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
    
    public final String[] submitSeekMessage(final String msg, final String provenance) {
        String[] resultStg = {""};
        // Traitement des messages entrant
        if (provenance.compareTo("momobot3") == 0) {
            // C'est un msg PV
            if (servSeek.toUpperCase().compareTo("on") == 0) {
                
                if (userpv.contains(provenance)) {
                    // Le bot a déja été PV par ce type
                    if (submitPVMessage(msg)) {
                        // OK le bot valide le pv <=> SEEK REUSSI
                        // On lui file ip pass
                        // + GOGOGO
                        seekInProgress = false;
                        resultStg = rajouteElement(resultStg, ippass);
                        resultStg = rajouteElement(resultStg, "GOGOGO");
                        return rajouteElement(resultStg, provenance + " a mordu! GOGOGO o//");
                    }
                    // REFOULE
                    return resultStg;
                    
                }
                // Le bot est PV pour la premiere fois >>
                // On le slap
                // Ensuite On lui demande si il est RDY
                userpv.add(provenance);
                resultStg = rajouteElement(resultStg, provenance);
                return rajouteElement(resultStg, "rdy?");
                
            } else if (servSeek.toUpperCase().compareTo("off") == 0) {
                // Le bot a déja été PV par ce bonhomme
                if (userpv.contains(provenance)) {
                    resultStg = rajouteElement(resultStg, "");
                    resultStg = rajouteElement(resultStg, "");
                    return rajouteElement(resultStg, provenance + " : " + msg);
                }
                // Le bot est PV pour la premiere fois
                if (submitPVMessage(msg)) {
                    // Le bot detecte un msg correct
                    // On PV le bonhomme ok > GO
                    // On affiche le msg (çad l'ip & pass) ds le channel de seek
                    userpv.add(provenance);
                    resultStg = rajouteElement(resultStg, "ok");
                    resultStg = rajouteElement(resultStg, "GO");
                    return rajouteElement(resultStg, provenance + " : " + msg);
                    
                }
                return resultStg;
            } else {
                // ERREUR INCONNU
                return resultStg;
            }
            
        } else {
            // C'est un msg d'un Channel
            
        }
        
        return resultStg;
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
