package momobot.whois;

import static java.lang.Math.floor;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import ircbot.IIrcCommands;
import ircbot.IrcUser;
import ircbot.QnetUser;
import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;

import utils.AbstractRunnable;

/**
 * @author mauhiz
 */
public class Whois extends AbstractRunnable implements IIrcCommands {
    /**
     * période minimale du whois en ms.
     */
    private static final long PERIOD     = 5000;
    /**
     * le temps d'attente entre deux boucles.
     */
    private static final int  SLEEPTIME  = 300;
    /**
     * le nombre de fois ou je peux boucler mon thread.
     */
    private static final int  Z_MAXTIMES = (int) floor(PERIOD / SLEEPTIME);
    /**
     * fait attendre la reponse.
     */
    private boolean           done;
    /**
     * si c'est un whois privé.
     */
    private final boolean     prive;
    /**
     * à qui renvoyer le résultat.
     */
    private final IrcUser     returnTo;
    /**
     * ma cible.
     */
    private final String      target;
    /**
     * le user sur lequel on whois (s'il existe).
     */
    private IrcUser           targetUser;

    /**
     * @param iu
     *            le user
     */
    public Whois(final IrcUser iu) {
        this(iu, false, null);
    }

    /**
     * @param target1
     *            la cible
     * @param prive1
     *            si c'est privé
     * @param sender1
     *            celui à qui renvoyer le résultat
     */
    public Whois(final IrcUser target1, final boolean prive1, final IrcUser sender1) {
        this(target1.getNick(), prive1, sender1);
        this.targetUser = target1;
    }

    /**
     * whois silencieux.
     * @param nick
     *            le nom
     */
    public Whois(final String nick) {
        this(nick, false, null);
    }

    /**
     * Envoie une demande de whois.
     * @param target1
     *            la cible
     * @param prive1
     *            si c'est privé
     * @param sender
     *            à qui renvoyer
     */
    public Whois(final String target1, final boolean prive1, final IrcUser sender) {
        super();
        this.target = target1;
        this.prive = prive1;
        this.returnTo = sender;
    }

    /**
     * @param string
     *            le message à afficher
     */
    public final void display(final String string) {
        if (this.returnTo != null) {
            if (this.prive) {
                MomoBot.getBotInstance().sendMessage(this.returnTo, string);
                return;
            }
            MomoBot.getBotInstance().sendNotice(this.returnTo, string);
        }
    }

    /**
     * Ce thread attend que momobot lui dise qu'il a fini le whois.
     */
    @Override
    public final void run() {
        /* le whois des bots de Qnet timeout */
        /* whois deja en cours */
        if (this.target.equalsIgnoreCase("L") || this.target.equalsIgnoreCase("Q") ||
                MomoBot.getBotInstance().isBeingWhoised(this.target)) {
            return;
        }
        /* fréquence maximale de whois */
        if (this.targetUser != null && currentTimeMillis() - this.targetUser.getLastWhois() < PERIOD) {
            return;
        }
        setRunning(true);
        MomoBot.getBotInstance().addWhois(this.target, this);
        MomoBot.getBotInstance().send(WHOIS + SPC + this.target + SPC + this.target);
        int loopCount = 0;
        while (isRunning()) {
            while (!this.done) {
                pause(SLEEPTIME);
                if (++loopCount >= Z_MAXTIMES) {
                    setRunning(false);
                    display("Le whois a échoué");
                    break;
                }
            }
            /* on suppose que le Whois a donc réussi => l'user existe. */
            this.targetUser = IrcUser.getUser(this.target);
            if (!(this.targetUser instanceof QnetUser)) {
                break;
            }
            if (StringUtils.isEmpty(((QnetUser) this.targetUser).getQnetAuth())) {
                display(this.target + " n'est pas authé sur Qnet.");
                setRunning(false);
                break;
            }
            display("L'auth Qnet de " + this.target + " est " + ((QnetUser) this.targetUser).getQnetAuth());
            /* debug */
            pause(MILLISECONDS.convert(1, SECONDS));
            setRunning(false);
        }
    }

    /**
     * @param boo
     *            si c'est fait.
     */
    public final void setDone(final boolean boo) {
        if (null == this.targetUser) {
            // le user s'est déconnecté on dirait.
            setRunning(false);
            return;
        }
        this.targetUser.setLastWhois(System.currentTimeMillis());
        this.done = boo;
    }

    /**
     * @param string
     *            l'auth à set
     */
    public final void setQnetAuth(final String string) {
        ((QnetUser) this.targetUser).setQnetAuth(string);
    }
}
