package net.mauhiz.irc.base.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Whois;
import net.mauhiz.util.AbstractRunnable;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class WhoisRequest extends AbstractRunnable {
    /**
     * tous les whois en cours.
     */
    private static final Map<String, WhoisRequest> ALL_WHOIS = new HashMap<String, WhoisRequest>();
    private static final Logger LOGGER = Logger.getLogger(WhoisRequest.class);
    
    /**
     * le temps d'attente entre deux boucles.
     */
    private static final long SLEEPTIME = 300;
    /**
     * periode minimale du whois en ms.
     */
    private static final long TIMEOUT = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS);
    
    public static void end(String nick, boolean ok) {
        WhoisRequest wr = ALL_WHOIS.get(nick);
        if (wr != null) {
            wr.setSuccess(ok);
        }
    }
    private final IIrcControl control;
    private boolean purgatory;
    private String reportTo;
    protected final IrcServer server;
    
    /**
     * fait attendre la reponse.
     */
    private boolean success;
    private final StopWatch sw = new StopWatch();
    /**
     * ma cible.
     */
    protected final String target;
    
    /**
     * whois silencieux.
     * 
     * @param nick
     *            le nom
     * @param server1
     * @param control1
     */
    public WhoisRequest(String nick, IrcServer server1, IIrcControl control1) {
        super();
        target = nick;
        server = server1;
        control = control1;
    }
    
    public long getElapsedTime() {
        return sw.getTime();
    }
    
    public String getReportTo() {
        return reportTo;
    }
    
    /**
     * Ce thread attend que momobot lui dise qu'il a fini le whois.
     */
    public void run() {
        /* whois deja en cours */
        IrcUser user = server.findUser(target, false);
        if (user == null) {
            /* user inconnu */
            purgatory = true;
        }
        /* frequence maximale de whois */
        WhoisRequest oldWr = ALL_WHOIS.get(target);
        /* whois en cours */
        if (oldWr != null && oldWr.getElapsedTime() < TIMEOUT) {
            /* whois precedent en cours */
            return;
            /* else : echec du whois precedent : retry */
        }
        
        waitForResult();
    }
    
    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }
    
    public void setSuccess(boolean ok) {
        stop();
        String respMsg;
        if (ok) {
            if (purgatory) {
                server.findUser(target, true);
            }
            respMsg = target + " has been whoised";
        } else {
            respMsg = "Could not whois " + target;
            // le user s'est deconnecte on dirait.
        }
        if (reportTo != null) {
            IIrcMessage resp = new Privmsg(null, reportTo, server, respMsg);
            control.sendMsg(resp);
        }
        sw.split();
        success = ok;
        LOGGER.debug("[whois ended][done=" + ok + "]");
        ALL_WHOIS.remove(target);
    }
    
    private void waitForResult() {
        sw.start();
        
        ALL_WHOIS.put(target, this);
        Whois who = new Whois(null, null, server, target);
        control.sendMsg(who);
        while (isRunning()) {
            while (!success) {
                pause(SLEEPTIME);
                if (sw.getTime() > TIMEOUT) {
                    /* echec du whois */
                    setSuccess(false);
                    return;
                }
            }
            
            /* debug */
            pause(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS));
        }
    }
}
