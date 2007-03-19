package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.Db;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class MemoTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public MemoTrigger(final String trigger) {
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
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        String msg = getArgs(message);
        if (msg.length() < 1) {
            MomoBot.getInstance().sendMessage(
                    channel,
                    "Je connais " + Db.countMemos()
                            + " memos. $listmemos pour avoir une liste");
            return;
        }
        final int i = msg.indexOf(" ");
        if (i < 1) {
            MomoBot.getInstance().sendMessage(channel, Db.getMemo(msg));
            return;
        }
        final String cle = msg.substring(0, i);
        msg = msg.substring(i + 1);
        MomoBot.getInstance().sendMessage(channel, Db.setMemo(cle, msg));
    }
}
