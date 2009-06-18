package net.mauhiz.irc.base.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Whois;
import net.mauhiz.util.AbstractRunnable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author mauhiz
 */
public class WhoisRequest extends AbstractRunnable {
    /**
     * tous les whois en cours.
     */
    private static final Map<String, WhoisRequest> ALL_WHOIS = new HashMap<String, WhoisRequest>();
    
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
    private final IrcServer server;
    
    /**
     * fait attendre la reponse.
     */
    private boolean success;
    private final StopWatch sw = new StopWatch();
    /**
     * ma cible.
     */
    private final String target;
    
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
    
    /**
     * @param nick
     * @param server1
     * @param control1
     * @param reportTo1
     */
    public WhoisRequest(String nick, IrcServer server1, IIrcControl control1, String reportTo1) {
        this(nick, server1, control1);
        reportTo = reportTo1;
    }
    
    /**
     * Ce thread attend que momobot lui dise qu'il a fini le whois.
     */
    public void run() {
        /* whois deja en cours */
        if (target.equalsIgnoreCase("L") || target.equalsIgnoreCase("Q")) {
            /* le whois des bots de Qnet timeout */
            return;
        }
        
        IrcUser user = server.findUser(target, false);
        if (user == null) {
            /* user inconnu */
            purgatory = true;
        }
        /* frequence maximale de whois */
        WhoisRequest oldWr = ALL_WHOIS.get(target);
        /* whois en cours */
        if (oldWr != null && oldWr.sw.getTime() < TIMEOUT) {
            /* whois precedent en cours */
            return;
            /* else : echec du whois precedent : retry */
        }
        
        waitForResult(user);
        
    }
    
    private void setSuccess(boolean ok) {
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
        IIrcMessage resp = new Privmsg(null, reportTo, server, respMsg);
        control.sendMsg(resp);
        sw.split();
        success = ok;
        LOG.debug("[whois ended][done=" + ok + "]");
        ALL_WHOIS.remove(target);
    }
    
    private void waitForResult(IrcUser user) {
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
            /* on suppose que le Whois a donc reussi => l'user existe. */
            if (!(user instanceof QnetUser)) {
                return;
            }
            QnetUser quser = (QnetUser) user;
            if (StringUtils.isEmpty(quser.getAuth())) {
                /* no auth */
                return;
            }
            // "L'auth Qnet de " + target + " est " + quser.getAuth()) //
            /* debug */
            pause(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS));
        }
    }
}
