package momobot.memo;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class MemoTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public MemoTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @Override
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        final String msg = getArgs(message);
        if (StringUtils.isEmpty(msg)) {
            MomoBot.getBotInstance().sendMessage(channel,
                    "Je connais " + DbMemoUtils.countMemos() + " memos. $listmemos pour avoir une liste");
            return;
        }
        final int index = msg.indexOf(' ');
        if (index < 1) {
            MomoBot.getBotInstance().sendMessage(channel, DbMemoUtils.getMemo(msg));
        } else {
            final String cle = msg.substring(0, index);
            MomoBot.getBotInstance().sendMessage(channel, DbMemoUtils.setMemo(cle, msg.substring(index + 1)));
        }
    }
}
