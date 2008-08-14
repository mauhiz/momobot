package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.MomoStringUtils;

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
    public static final String channels = "#-hp-;#-duck-";
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(SeekWar.class);
    /**
     * black list pour un msg pv
     */
    private String[] blackList = {"www", "http", "://", ".com", ".fr", ".eu", ".info"};
    /**
     * gather
     */
    private Gather gather;
    /**
     * 
     */
    private String[] greenList = {"ok", "oui", "go", "k", "ip", "pass", "yes"};
    /**
     * IpPass du srv
     */
    private String ippass;
    /**
     * True si le seek est en cour ; false sinon
     */
    private boolean seekInProgress;
    /**
     * level seek
     */
    private String seekLevel;
    /**
     * Message de seek %P=nb Player ; %S=Serv ON/OFF ; %L = level
     */
    private String seekMessage;
    /**
     * si le gather a un serv de jeu ou pass
     */
    private String seekServ;
    /**
     * Temps de time out
     */
    private long seekTimeOut;
    /**
     * le temps où je commence.
     */
    private final StopWatch sw = new StopWatch();
    /**
     * Liste des users qui ont pv le bot
     */
    private ArrayList<String> userpv = new ArrayList<String>();
    /**
     * 
     * @param gath
     *            = gather qui est propriétaire de SeekWar()
     */
    public SeekWar(final Gather gath) {
        seekInProgress = false;
        seekServ = "ON";
        ippass = "87.98.196.75:27019 Gotserv.com: pw:gruik";
        seekLevel = "mid";
        seekMessage = "seek %Pv%P - %S - %L pm ";
        gather = gath;
        seekTimeOut = 7 * 60 * 10000; // 7min
        userpv.clear();
    }
    public final String getMessageForSeeking() {
        return MomoStringUtils.genereSeekMessage(seekMessage, gather.getNumberPlayers(), seekServ, seekLevel);
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
        System.out.println("Time seek : " + sw.getTime());
        if (sw.getTime() < seekTimeOut) {
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
                if (!(element.charAt(0) == '\"' && element.charAt(element.length() - 1) == '\"')) {
                    Inquote = !Inquote;
                }
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
                    seekServ = cmdSeek[0];
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 2 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek[0];
                    ippass = cmdSeek[1];
                    return "Seek : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek[0];
                    seekLevel = cmdSeek[1];
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 3 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek[0];
                    ippass = cmdSeek[1];
                    seekLevel = cmdSeek[2];
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek[0].toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek[0];
                    seekLevel = cmdSeek[1];
                    seekMessage = cmdSeek[2];
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel
                            + " MSGSeek = " + seekMessage;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
            }
                
            case 4 : {
                if (cmdSeek[0].toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek[0];
                    ippass = cmdSeek[1];
                    seekLevel = cmdSeek[2];
                    seekMessage = cmdSeek[3];
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel
                            + " MSGSeek = " + seekMessage;
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
        userpv.clear();
        sw.stop();
        sw.reset();
        return "Le seek est arrete.";
    }
    /**
     * @param stg
     *            = Message qui vient du channel de seek
     * @return
     * 
     */
    public final boolean submitChannelMessage(final String stg) {
        // On doit inverser le seek ex:Si je seek srv ON,le msg de match doit être serv off.
        String seekServ1 = "";
        if (seekServ.toLowerCase().compareTo("on") == 0) {
            seekServ1 = "off";
        } else if (seekServ.toLowerCase().compareTo("off") == 0) {
            seekServ1 = "on";
        }
        
        if ((stg.toLowerCase().contains(gather.getNumberPlayers() + "vs" + gather.getNumberPlayers())
                || stg.toLowerCase().contains(gather.getNumberPlayers() + "v" + gather.getNumberPlayers())
                || stg.toLowerCase().contains(gather.getNumberPlayers() + "o" + gather.getNumberPlayers()) || stg
                .toLowerCase().contains(gather.getNumberPlayers() + "on" + gather.getNumberPlayers()))
                && stg.toLowerCase().contains(seekServ1) && stg.toLowerCase().contains(seekLevel.toLowerCase())) {
            return true;
        }
        return false;
    }
    /**
     * @param msg
     * @return true si le bot pense que le PV est ok
     */
    private final boolean submitPVMessage(final String msg) {
        
        // RAJOUTE UNE DETECTION D'UNE IP&PASS
        
        for (String element : blackList) {
            if (msg.toLowerCase().contains(element)) {
                return false;
            }
        }
        for (String element : greenList) {
            if (msg.toLowerCase().contains(element)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param msg
     * @param destination
     *            // momobot3 ou #channel
     * @param provenance
     *            // nick de l'user qui a parler
     * @return String
     */
    
    public final String[] submitSeekMessage(final String msg, final String destination, final String provenance) {
        String[] resultStg = new String[0];
        // Traitement des messages entrant
        if (destination.compareTo("momobot3") == 0) {
            // C'est un msg PV
            if (seekServ.toLowerCase().compareTo("on") == 0) {
                
                if (userpv.contains(provenance)) {
                    // Le bot a déja été PV par ce type
                    if (submitPVMessage(msg)) {
                        // OK le bot valide le pv <=> SEEK REUSSI
                        // On lui file ip pass
                        // + GOGOGO
                        resultStg = rajouteElement(resultStg, ippass);
                        resultStg = rajouteElement(resultStg, "GOGOGO");
                        resultStg = rajouteElement(resultStg, provenance + " a mordu! GOGOGO o//");
                        seekInProgress = false;
                        userpv.clear();
                        sw.stop();
                        sw.reset();
                        return resultStg;
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
                
            } else if (seekServ.toLowerCase().compareTo("off") == 0) {
                // Le bot a déja été PV par ce bonhomme
                if (userpv.contains(provenance)) {
                    resultStg = rajouteElement(resultStg, "");
                    resultStg = rajouteElement(resultStg, "");
                    resultStg = rajouteElement(resultStg, provenance + " :" + msg);
                    return resultStg;
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
            
        }
        // C'est un msg d'un Channel
        if (submitChannelMessage(msg)) {
            {
                if (userpv.contains(provenance)) {
                    // Cas chelou :O ??!??!!??
                    resultStg = rajouteElement(resultStg, "go?");
                    // seekInProgress = false;
                    // userpv.clear();
                    // sw.stop();
                    // sw.reset();
                    return resultStg;
                }
                // On pv le mec pr lui dire rdy?
                resultStg = rajouteElement(resultStg, "rdy?");
                userpv.add(provenance);
                return resultStg;
            }
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
