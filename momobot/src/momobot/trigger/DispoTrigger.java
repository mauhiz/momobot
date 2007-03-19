package momobot.trigger;

import ircbot.IrcUser;
import ircbot.QnetUser;
import ircbot.ATrigger;
import java.util.Date;
import java.util.StringTokenizer;
import momobot.Db;
import momobot.MomoBot;
import momobot.Whois;
import utils.Utils;

/**
 * @author Administrator
 */
public class DispoTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public DispoTrigger(final String trigger) {
        super(trigger);
        setPublic(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        // rien
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     * @param channel
     *            le channel
     * @param user
     *            le user
     * @param message
     *            le message
     */
    @Override
    public final synchronized void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        if (!(user instanceof QnetUser)) {
            Utils.log(getClass(), "user non Qnet");
            return;
        }
        if (((QnetUser) user).getQnetAuth().length() == 0) {
            final Whois w = new Whois(user.getNick());
            w.execute();
            // on attend le whois
            while (w.isRunning()) {
                Thread.yield();
            }
            // TODO : bug a la con de merde, il detecte pas le whois tout de
            // suite :/
            if (((QnetUser) user).getQnetAuth().length() == 0) {
                MomoBot
                        .getInstance()
                        .sendNotice(user,
                                "il faut etre auth sur Qnet pour utiliser cette fonction");
                return;
            }
        }
        try {
            final StringTokenizer st = new StringTokenizer(getArgs(message));
            final String jour = st.nextToken();
            final String[] heure = new String[2];
            heure[0] = st.nextToken();
            heure[1] = st.nextToken();
            // tokens finis
            final Date d = Utils.getDateFromJour(jour);
            final int[] h = new int[2];
            for (int i = 0; i < 2; i++) {
                if (heure[i].equalsIgnoreCase("oui")) {
                    h[i] = 1;
                    continue;
                }
                if (heure[i].equalsIgnoreCase("non")) {
                    h[i] = -1;
                    continue;
                }
            }
            Db.updateDispo(channel, ((QnetUser) user).getQnetAuth(), d
                    .getTime(), h[0], h[1]);
            MomoBot.getInstance().sendNotice(user,
                    "dispo enregistree pour le " + Utils.MY_DATE.format(d));
        } catch (final Exception e) {
            MomoBot
                    .getInstance()
                    .sendNotice(user,
                            "syntaxe : $dispo jour[lundi/mardi/...] 21h[oui/non/?] 22h30[oui/non/?]");
        }
    }
}
