package net.mauhiz.irc.bot.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.MomoStringUtils;
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
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(SeekWar.class);
    /**
     * liste des channels de seek
     */
    public static final String[] SEEK_CHANS = {"#-hp-", "#-duck-", "#clanwar.fr"};
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
    private final List<String> userpv = new ArrayList<String>();
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
        // 7 min
        seekTimeOut = TimeUnit.MILLISECONDS.convert(7, TimeUnit.MINUTES);
        userpv.clear();
    }
    
    /**
     * @return
     */
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
     * @param cmd
     *            String[] non normalise
     * @return un message
     */
    List<String> split(final String[] cmd) {
        List<String> cmdNormalise = new ArrayList<String>();
        String tmpStg = "";
        
        boolean inquote = false;
        for (String element : cmd) {
            
            if (element.charAt(0) == '\"' || element.charAt(element.length() - 1) == '\"') {
                if (!(element.charAt(0) == '\"' && element.charAt(element.length() - 1) == '\"')) {
                    inquote = !inquote;
                }
            }
            
            if (tmpStg.isEmpty()) {
                tmpStg = element;
            } else {
                tmpStg += " " + element;
            }
            if (!inquote) {
                cmdNormalise.add(tmpStg);
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
        
        List<String> cmdSeek = split(commandSeek);
        
        // On CFG le seek avec les param
        switch (cmdSeek.size()) {
            case 0 :
                // Seek sans parametre
                seekInProgress = true;
                return "Seek Par Defaut.";
                
            case 1 :
                if (cmdSeek.get(0).toLowerCase().compareTo("on") == 0
                        || cmdSeek.get(0).toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                }
                sw.stop();
                sw.reset();
                return "Paramètre(s) Incorrect";
                
            case 2 :
                if (cmdSeek.get(0).toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    ippass = cmdSeek.get(1);
                    return "Seek : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek.get(0).toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    seekLevel = cmdSeek.get(1);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
                
            case 3 :
                if (cmdSeek.get(0).toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    ippass = cmdSeek.get(1);
                    seekLevel = cmdSeek.get(2);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek.get(0).toLowerCase().compareTo("off") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    seekLevel = cmdSeek.get(1);
                    seekMessage = cmdSeek.get(2);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel
                            + " MSGSeek = " + seekMessage;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
                
            case 4 :
                if (cmdSeek.get(0).toLowerCase().compareTo("on") == 0) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    ippass = cmdSeek.get(1);
                    seekLevel = cmdSeek.get(2);
                    seekMessage = cmdSeek.get(3);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel
                            + " MSGSeek = " + seekMessage;
                }
                sw.stop();
                sw.reset();
                return "Paramètre(s) Incorrect";
                
            default :
                sw.stop();
                sw.reset();
                return "Paramètre(s) Incorrect";
                
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
     *            Message qui vient du channel de seek
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
    private boolean submitPVMessage(final String msg) {
        
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
     *            momobot3 ou #channel
     * @param from
     *            nick de l'user qui a parler
     * @return String
     */
    
    public final List<String> submitSeekMessage(final String msg, final String destination, final IrcUser from) {
        String provenance = from.getNick();
        List<String> resultStg = new ArrayList<String>(3);
        // Traitement des messages entrant
        if ("momobot3".equals(destination)) {
            // C'est un msg PV
            if ("on".equals(seekServ.toLowerCase())) {
                
                if (userpv.contains(provenance)) {
                    // Le bot a déja été PV par ce type
                    if (submitPVMessage(msg)) {
                        // OK le bot valide le pv <=> SEEK REUSSI
                        // On lui file ip pass
                        // + GOGOGO
                        resultStg.add(ippass);
                        resultStg.add("GOGOGO");
                        resultStg.add(provenance + " a mordu! GOGOGO o//");
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
                resultStg.add(provenance);
                resultStg.add("rdy?");
                return resultStg;
                
            } else if (seekServ.toLowerCase().compareTo("off") == 0) {
                // Le bot a déja été PV par ce bonhomme
                if (userpv.contains(provenance)) {
                    resultStg.add("");
                    resultStg.add("");
                    resultStg.add(provenance + " :" + msg);
                    return resultStg;
                }
                // Le bot est PV pour la premiere fois
                if (submitPVMessage(msg)) {
                    // Le bot detecte un msg correct
                    // On PV le bonhomme ok > GO
                    // On affiche le msg (çad l'ip & pass) ds le channel de seek
                    userpv.add(provenance);
                    resultStg.add("ok");
                    resultStg.add("GO");
                    resultStg.add(provenance + " : " + msg);
                    return resultStg;
                    
                }
                return resultStg;
            } else {
                // ERREUR INCONNU
                return resultStg;
            }
            
        }
        // C'est un msg d'un Channel
        if (submitChannelMessage(msg)) {
            
            if (userpv.contains(provenance)) {
                // Cas chelou :O ??!??!!??
                resultStg.add("go?");
                // seekInProgress = false;
                // userpv.clear();
                // sw.stop();
                // sw.reset();
                return resultStg;
            }
            // On pv le mec pr lui dire rdy?
            resultStg.add("rdy?");
            userpv.add(provenance);
            return resultStg;
            
        }
        
        return resultStg;
    }
}
