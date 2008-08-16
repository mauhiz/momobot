package net.mauhiz.irc.base.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Whois;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class WhoisRequest extends AbstractRunnable {
    /**
     * tous les whois en cours.
     */
    private static final Map<IrcUser, WhoisRequest> allWhois = new HashMap<IrcUser, WhoisRequest>();
    /**
     * logger
     */
    private static final Logger LOG = Logger.getLogger(WhoisRequest.class);
    /**
     * le temps d'attente entre deux boucles.
     */
    private static final long SLEEPTIME = 300;
    /**
     * période minimale du whois en ms.
     */
    private static final long TIMEOUT = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS);
    private final IIrcControl control;
    private boolean purgatory;
    private String reportTo;
    /**
     * resultat
     */
    private WhoisResult result;
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
    public WhoisRequest(final String nick, final IrcServer server1, final IIrcControl control1) {
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
    public WhoisRequest(final String nick, final IrcServer server1, final IIrcControl control1, final String reportTo1) {
        this(nick, server1, control1);
        reportTo = reportTo1;
    }
    
    /**
     * Ce thread attend que momobot lui dise qu'il a fini le whois.
     */
    public final void run() {
        /* whois deja en cours */
        if (target.equalsIgnoreCase("L") || target.equalsIgnoreCase("Q")) {
            /* le whois des bots de Qnet timeout */
            return;
        }
        
        IrcUser user = Users.get(server).findUser(target, false);
        if (user == null) {
            /* user inconnu */
            user = new IrcUser(target);
            purgatory = true;
        }
        /* fréquence maximale de whois */
        WhoisRequest oldWr = allWhois.get(user);
        /* whois en cours */
        if (oldWr != null && oldWr.sw.getTime() < TIMEOUT) {
            return;
            /* echec du whois precedent */
        }
        
        sw.start();
        
        setRunning(true);
        allWhois.put(user, this);
        Whois who = new Whois(null, null, server, target);
        control.sendMsg(who);
        while (isRunning()) {
            while (!success) {
                pause(SLEEPTIME);
                if (sw.getTime() > TIMEOUT) {
                    setRunning(false);
                    /* echec du whois */
                    break;
                }
            }
            /* on suppose que le Whois a donc réussi => l'user existe. */
            if (!(user instanceof QnetUser)) {
                break;
            }
            QnetUser quser = (QnetUser) user;
            if (StringUtils.isEmpty(quser.getAuth())) {
                /* no auth */
                break;
            }
            // "L'auth Qnet de " + target + " est " + quser.getAuth()) //
            /* debug */
            pause(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS));
        }
        setRunning(false);
    }
    
    /**
     * @param boo
     *            si c'est fait.
     */
    public final void setSuccess(final boolean boo) {
        setRunning(false);
        String respMsg;
        if (boo) {
            respMsg = result.result[0];
            if (purgatory) {
                Users.get(server).findUser(target, true);
            }
        } else {
            respMsg = "Could not whois " + target;
            // le user s'est déconnecté on dirait.
        }
        IIrcMessage resp = new Privmsg(null, reportTo, server, respMsg);
        control.sendMsg(resp);
        sw.split();
        LOG.debug("[whois ended][done=" + boo + "]");
        success = boo;
    }
}
