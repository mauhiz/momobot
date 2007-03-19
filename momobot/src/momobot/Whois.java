package momobot;

import ircbot.IIrcCommands;
import ircbot.IrcUser;
import ircbot.QnetUser;
import java.util.concurrent.TimeUnit;
import utils.MyRunnable;

/**
 * @author Administrator
 */
public class Whois extends MyRunnable implements IIrcCommands {
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
    private static final int  Z_MAXTIMES = (int) Math.floor(PERIOD / SLEEPTIME);
    /**
     * fait attendre la reponse.
     */
    private boolean           done       = false;
    /**
     * si c'est un whois privé.
     */
    private boolean           prive      = false;
    /**
     * à qui renvoyer le résultat.
     */
    private IrcUser           returnTo   = null;
    /**
     * ma cible.
     */
    private final String      target;
    /**
     * le user sur lequel on whois (s'il existe).
     */
    private QnetUser          user       = null;

    /**
     * @param user1
     *            le user
     */
    public Whois(final QnetUser user1) {
        this(user1, false, null);
    }

    /**
     * @param target1
     *            la cible
     * @param prive1
     *            si c'est privé
     * @param sender1
     *            celui à qui renvoyer le résultat
     */
    public Whois(final QnetUser target1, final boolean prive1,
            final IrcUser sender1) {
        this(target1.getNick(), prive1, sender1);
        this.user = target1;
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
    public Whois(final String target1, final boolean prive1,
            final IrcUser sender) {
        this.target = target1;
        this.prive = prive1;
        this.returnTo = sender;
    }

    /**
     * @param s
     *            le message à afficher
     */
    public final void display(final String s) {
        if (this.returnTo == null) {
            return;
        }
        if (this.prive) {
            MomoBot.getInstance().sendMessage(this.returnTo.getNick(), s);
            return;
        }
        MomoBot.getInstance().sendNotice(this.returnTo, s);
    }

    /**
     * Ce thread attend que momobot lui dise qu'il a fini le whois.
     */
    @Override
    public final void run() {
        setRunning(true);
        // le whois des bots de Qnet timeout
        if (this.target.equalsIgnoreCase("L")
                || this.target.equalsIgnoreCase("Q")) {
            setRunning(false);
            return;
        }
        if (this.user != null) {
            // fréquence maximale de whois
            if (System.currentTimeMillis() - this.user.getLastWhois() < PERIOD) {
                setRunning(false);
                return;
            }
        }
        // whois deja en cours
        if (MomoBot.getInstance().isBeingWhoised(this.target)) {
            setRunning(false);
            return;
        }
        MomoBot.getInstance().addWhois(this.target, this);
        MomoBot.getInstance().send(
                WHOIS + SPC + this.target + SPC + this.target);
        int i = 0;
        while (isRunning()) {
            while (!this.done) {
                sleep(SLEEPTIME);
                if (++i >= Z_MAXTIMES) {
                    setRunning(false);
                    display("Le whois a échoué");
                    break;
                }
            }
            // on suppose que le Whois a donc réussi => l'user existe.
            this.user = (QnetUser) IrcUser.getUser(this.target);
            if (this.user.getQnetAuth().length() == 0) {
                display(this.target + " n'est pas authé sur Qnet.");
                setRunning(false);
                break;
            }
            display("L'auth Qnet de " + this.target + " est "
                    + this.user.getQnetAuth());
            // debug
            try {
                Thread
                        .sleep(TimeUnit.MILLISECONDS.convert(1,
                                TimeUnit.SECONDS));
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            setRunning(false);
        }
    }

    /**
     * @param b
     *            si c'est fait.
     */
    public final void setDone(final boolean b) {
        if (this.user == null) {
            // le user s'est déconnecté on dirait.
            setRunning(false);
            return;
        }
        this.user.setLastWhois(System.currentTimeMillis());
        this.done = b;
    }

    /**
     * @param string
     *            l'auth à set
     */
    public final void setQnetAuth(final String string) {
        this.user.setQnetAuth(string);
    }
}
