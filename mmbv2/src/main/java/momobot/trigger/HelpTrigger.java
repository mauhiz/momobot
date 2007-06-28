package momobot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.INoticeTrigger;
import ircbot.trigger.IPriveTrigger;
import ircbot.trigger.IPublicTrigger;
import ircbot.trigger.ITrigger;
import momobot.MomoBot;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class HelpTrigger extends AbstractTrigger implements IPublicTrigger, IPriveTrigger, INoticeTrigger {
    /**
     * message.
     */
    private final StrBuilder    msg       = new StrBuilder();
    /**
     * Commandes :.
     */
    private static final String COMMANDES = "Commandes :";

    /**
     * @param trigger
     *            le trigger
     */
    public HelpTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        if (!test(message)) {
            return;
        }
        this.msg.clear().append(COMMANDES);
        for (final ITrigger trig : AbstractTrigger.getTriggers()) {
            if (trig instanceof IPriveTrigger) {
                this.msg.append(trig).append(' ');
            }
        }
        MomoBot.getBotInstance().sendMessage(user, this.msg.toString());
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @Override
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        this.msg.clear().append(COMMANDES);
        for (final ITrigger trig : AbstractTrigger.getTriggers()) {
            if (trig instanceof IPublicTrigger) {
                this.msg.append(trig).append(' ');
            }
        }
        MomoBot.getBotInstance().sendNotice(user, this.msg.toString());
    }

    /**
     * @see ircbot.trigger.INoticeTrigger#executeNoticeTrigger(ircbot.IrcUser, java.lang.String)
     */
    @Override
    public void executeNoticeTrigger(final IrcUser user, final String message) {
        if (!test(message)) {
            return;
        }
        this.msg.clear().append(COMMANDES);
        for (final ITrigger trig : AbstractTrigger.getTriggers()) {
            if (trig instanceof INoticeTrigger) {
                this.msg.append(trig).append(' ');
            }
        }
        MomoBot.getBotInstance().sendMessage(user, this.msg.toString());
    }
}
