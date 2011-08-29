package net.mauhiz.irc.base.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Whois;
import net.mauhiz.util.AbstractDaemon;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class WhoisRequest extends AbstractDaemon {
    /**
     * tous les whois en cours.
     */
    private static final Map<String, WhoisRequest> ALL_WHOIS = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(WhoisRequest.class);

    /**
     * le temps d'attente entre deux boucles.
     */
    private static final long SLEEPTIME = 300;
    /**
     * periode minimale du whois en ms.
     */
    private static final long TIMEOUT = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS);

    public static void end(ArgumentList args, boolean ok) {
        for (String whois : args) {
            WhoisRequest wr = get(whois);
            if (wr != null) {
                wr.setSuccess(ok);
            }
        }
    }

    public static WhoisRequest get(String target2) {
        return ALL_WHOIS.get(target2);
    }

    public static void startWhois(IIrcServerPeer server, IIrcControl control, Iterable<String> args, Target reportTo) {
        for (String arg : args) {
            WhoisRequest wr = new WhoisRequest(server, control, arg);
            wr.setReportTo(reportTo);
            wr.tstart();
        }
    }

    private final IIrcControl control;
    private boolean purgatory;

    private Target reportTo;
    protected final IIrcServerPeer server;
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
     */
    protected WhoisRequest(IIrcServerPeer server, IIrcControl control, String nick) {
        super("Whois Request");
        target = nick;
        this.server = server;
        this.control = control;
    }

    public long getElapsedTime() {
        return sw.getTime();
    }

    public Target getReportTo() {
        return reportTo;
    }

    public void setReportTo(Target reportTo) {
        this.reportTo = reportTo;
    }

    public void setSuccess(boolean ok) {
        tstop();
        String respMsg;
        if (ok) {
            if (purgatory) {
                server.getNetwork().findUser(target, true);
            }
            respMsg = target + " has been whoised";
        } else {
            respMsg = "Could not whois " + target;
            // le user s'est deconnecte on dirait.
        }
        if (reportTo != null) {
            IIrcMessage resp = new Privmsg(server, null, reportTo, respMsg);
            control.sendMsg(resp);
        }
        sw.split();
        success = ok;
        LOGGER.debug("[whois ended][done=" + ok + "]");
        ALL_WHOIS.remove(target);
    }

    /**
     * Ce thread attend que momobot lui dise qu'il a fini le whois.
     */
    @Override
    public void trun() {
        /* whois deja en cours */
        IrcUser user = server.getNetwork().findUser(target, false);
        if (user == null) {
            /* user inconnu */
            purgatory = true;
        }
        /* frequence maximale de whois */
        WhoisRequest oldWr = get(target);
        /* whois en cours */
        if (oldWr != null && oldWr.getElapsedTime() < TIMEOUT) {
            /* whois precedent en cours */
            return;
            /* else : echec du whois precedent : retry */
        }

        waitForResult();
    }

    private void waitForResult() {
        sw.start();

        ALL_WHOIS.put(target, this);
        Whois who = new Whois(server, null, target);
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
