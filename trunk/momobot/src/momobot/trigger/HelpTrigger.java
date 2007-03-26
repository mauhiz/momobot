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
        StringBuffer msg = new StringBuffer("Commandes : ");
        for (ATrigger t : ATrigger.getTriggers()) {
            if (t.isPrive()) {
                msg.append(t.getTriggerText());
                msg.append(' ');
            }
        }
        MomoBot.getInstance().sendMessage(user.getNick(), msg.toString());
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        StringBuffer msg = new StringBuffer("Commandes : ");
        for (ATrigger t : ATrigger.getTriggers()) {
            if (t.isPublic()) {
                msg.append(t.getTriggerText());
                msg.append(' ');
            }
        }
        MomoBot.getInstance().sendNotice(user, msg.toString());
    }
}
