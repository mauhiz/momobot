package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import java.util.Iterator;
import momobot.MomoBot;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class HelpTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public HelpTrigger(final String trigger) {
        super(trigger);
        setPublic(true);
        setPrive(true);
        setNotice(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        final Iterator < ATrigger > ite = ATrigger.getTriggers();
        String msg = "Commandes : ";
        while (ite.hasNext()) {
            final ATrigger t = ite.next();
            if (t.isPrive()) {
                msg += t.getTriggerText() + " ";
            }
        }
        MomoBot.getInstance().sendMessage(user.getNick(), msg);
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        final Iterator < ATrigger > ite = ATrigger.getTriggers();
        String msg = "Commandes : ";
        while (ite.hasNext()) {
            final ATrigger t = ite.next();
            if (t.isPublic()) {
                msg += t.getTriggerText() + " ";
            }
        }
        MomoBot.getInstance().sendNotice(user, msg);
    }
}
