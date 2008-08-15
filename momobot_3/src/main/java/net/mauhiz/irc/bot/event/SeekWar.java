package net.mauhiz.irc.bot.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;

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
    public static final String[] SEEK_CHANS = {"#clanwar.fr"};
    /**
     * black list pour un msg pv
     */
    private String[] blackList = {"www", "http", "://", ".com", ".fr", ".eu", ".info"};
    /**
     * channel sur lequel le seek a ete lance
     */
    private String channel = "";
    /**
     * gather
     */
    private Gather gather;
    /**
     * 
     */
    private String[] greenList = {"ok", "oui", "go", "k", "ip", "pass", "yes", "lvl", "level", "yep", "moi"};
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
     * si on a splité
     */
    private boolean splited = false;
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
        seekTimeOut = TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES);
        userpv.clear();
    }
    
    public final String getChannel() {
        return channel;
    }
    
    /**
     * @return
     */
    public final String getMessageForSeeking() {
        return MomoStringUtils.genereSeekMessage(seekMessage, gather.getNumberPlayers(), seekServ, seekLevel);
    }
    public final String getSeekWinner() {
        if (userpv.isEmpty()) {
            return "";
        }
        return userpv.get(0);
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
    public String start(final String[] commandSeek, final String chan) {
        sw.start();
        channel = chan;
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
                if (cmdSeek.get(0).toLowerCase().equals("on") || cmdSeek.get(0).toLowerCase().equals("off")) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                }
                sw.stop();
                sw.reset();
                return "Paramètre(s) Incorrect";
                
            case 2 :
                if (cmdSeek.get(0).toLowerCase().equals("on")) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    ippass = cmdSeek.get(1);
                    return "Seek : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek.get(0).toLowerCase().equals("off")) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    seekLevel = cmdSeek.get(1);
                    return "Seek - Info : serv = " + seekServ + " level = " + seekLevel;
                } else {
                    sw.stop();
                    sw.reset();
                    return "Paramètre(s) Incorrect";
                }
                
            case 3 :
                if (cmdSeek.get(0).toLowerCase().equals("on")) {
                    seekInProgress = true;
                    seekServ = cmdSeek.get(0);
                    ippass = cmdSeek.get(1);
                    seekLevel = cmdSeek.get(2);
                    return "Seek - Info : serv = " + seekServ + " ippass = " + ippass + " level = " + seekLevel;
                } else if (cmdSeek.get(0).toLowerCase().equals("off")) {
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
                if (cmdSeek.get(0).toLowerCase().equals("on")) {
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
        if (seekServ.toLowerCase().equals("on")) {
            seekServ1 = "off";
        } else if (seekServ.toLowerCase().equals("off")) {
            seekServ1 = "on";
        }
        
        String[] separateur = {"vs", "v", "on", "o", "x"};
        ArrayList<String> listMatch = new ArrayList<String>();
        int player = gather.getNumberPlayers();
        for (String element : separateur) {
            listMatch.add(player + element + player);
            listMatch.add(player + " " + element + " " + player);
        }
        boolean numbermatch = false;
        for (String element : listMatch) {
            if (stg.toLowerCase().contains(element)) {
                numbermatch = true;
                break;
            }
            
        }
        
        if (numbermatch && stg.toLowerCase().contains(seekServ1) && stg.toLowerCase().contains(seekLevel.toLowerCase())) {
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
    
    public final List<Privmsg> submitSeekMessage(final Privmsg im) {
        IrcServer server1 = im.getServer();
        IrcUser kikoolol = Users.get(im.getServer()).findUser(new Mask(im.getFrom()), true);
        String provenance = kikoolol.getNick();
        String destination = im.getTo();
        String msg = im.getMessage();
        List<Privmsg> resultPrivmsg = new ArrayList<Privmsg>(3);
        
        // On test si il faut renvoié le msg de seek au channel
        
        if (sw.getTime() > TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)) {
            if (splited) {
                if (sw.getSplitTime() > TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)) {
                    sw.split();
                    seekMessage = "." + seekMessage + ".";
                    for (String element : SEEK_CHANS) {
                        Privmsg msg1 = new Privmsg(null, element, im.getServer(), getMessageForSeeking());
                        resultPrivmsg.add(msg1);
                    }
                }
            } else {
                sw.split();
                splited = true;
                seekMessage = "." + seekMessage + ".";
                Privmsg msg3 = new Privmsg(null, channel, im.getServer(), getMessageForSeeking());
                resultPrivmsg.add(msg3);
            }
        }
        
        // Traitement des messages entrant
        if ("mom0".equals(destination)) {
            // C'est un msg PV
            if ("on".equals(seekServ.toLowerCase())) {
                
                if (userpv.contains(provenance)) {
                    // Le bot a déja été PV par ce type
                    if (submitPVMessage(msg)) {
                        // OK le bot valide le pv <=> SEEK REUSSI
                        // On lui file ip pass
                        // + GOGOGO
                        if (msg.toLowerCase().contains("lvl") || msg.toLowerCase().contains("level")) {
                            
                            Privmsg msg1 = Privmsg.buildPrivateAnswer(im, seekLevel);
                            resultPrivmsg.add(msg1);
                            Privmsg msg2 = Privmsg.buildPrivateAnswer(im, "go?");
                            resultPrivmsg.add(msg2);
                            
                        }
                        Privmsg msg1 = Privmsg.buildPrivateAnswer(im, ippass);
                        resultPrivmsg.add(msg1);
                        Privmsg msg2 = Privmsg.buildPrivateAnswer(im, "GOGOGO");
                        resultPrivmsg.add(msg2);
                        Privmsg msg3 = new Privmsg(null, channel, im.getServer(), provenance + " a mordu! GOGOGO o//");
                        resultPrivmsg.add(msg3);
                        seekInProgress = false;
                        userpv.clear();
                        userpv.add(provenance);
                        sw.stop();
                        sw.reset();
                        return resultPrivmsg;
                    }
                    // REFOULE
                    return resultPrivmsg;
                    
                }
                // Le bot est PV pour la premiere fois >>
                // On le slap
                // Ensuite On lui demande si il est RDY
                userpv.add(provenance);
                Privmsg msg1 = Privmsg.buildPrivateAnswer(im, provenance);
                resultPrivmsg.add(msg1);
                Privmsg msg2 = Privmsg.buildPrivateAnswer(im, "rdy?");
                resultPrivmsg.add(msg2);
                return resultPrivmsg;
                
            } else if (seekServ.toLowerCase().compareTo("off") == 0) {
                // Le bot a déja été PV par ce bonhomme
                if (userpv.contains(provenance)) {
                    Privmsg msg1 = new Privmsg(null, channel, im.getServer(), provenance + " :" + msg);
                    resultPrivmsg.add(msg1);
                    return resultPrivmsg;
                }
                // Le bot est PV pour la premiere fois
                if (submitPVMessage(msg)) {
                    // Le bot detecte un msg correct
                    // On PV le bonhomme ok > GO
                    // On affiche le msg (çad l'ip & pass) ds le channel de seek
                    userpv.add(provenance);
                    Privmsg msg1 = Privmsg.buildPrivateAnswer(im, "ok");
                    resultPrivmsg.add(msg1);
                    Privmsg msg2 = Privmsg.buildPrivateAnswer(im, "GO");
                    resultPrivmsg.add(msg2);
                    Privmsg msg3 = new Privmsg(null, channel, im.getServer(), provenance + " :" + msg);
                    resultPrivmsg.add(msg3);
                    return resultPrivmsg;
                    
                }
                return resultPrivmsg;
            } else {
                // ERREUR INCONNU
                return resultPrivmsg;
            }
            
        }
        // C'est un msg d'un Channel
        if (submitChannelMessage(msg)) {
            
            if (userpv.contains(provenance)) {
                // Cas chelou :O ??!??!!??
                Privmsg msg1 = Privmsg.buildPrivateAnswer(im, "go?");
                resultPrivmsg.add(msg1);
                // seekInProgress = false;
                // userpv.clear();
                // userpv.add(provenance);
                // sw.stop();
                // sw.reset();
                return resultPrivmsg;
            }
            // On pv le mec pr lui dire rdy?
            Privmsg msg1 = Privmsg.buildPrivateAnswer(im, "rdy?");
            resultPrivmsg.add(msg1);
            if (seekLevel.toLowerCase().equals("off")) {
                userpv.add("ip&pass?");
            }
            return resultPrivmsg;
            
        }
        
        return resultPrivmsg;
    }
    public final String tg() {
        userpv.clear();
        return "Ok je la ferme.";
    }
}
