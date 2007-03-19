package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import java.util.Iterator;
import java.util.Map;
import momobot.Db;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class KnownPlayersTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public KnownPlayersTrigger(final String trigger) {
        super(trigger);
        setPrive(true);
        setOnlyAdmin(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        MomoBot.getInstance().sendMessage(user.getNick(),
                "Je connais " + Db.countPlayers() + " joueur(s).");
        for (final Iterator < Map.Entry < String, String >> ite = Db
                .getPlayers(); ite.hasNext();) {
            final Map.Entry < String, String > item = ite.next();
            MomoBot.getInstance().sendMessage(user.getNick(),
                    item.getValue() + " - " + item.getKey());
        }
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        // rien
    }
}
