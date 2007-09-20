package momobot.trigger;

import momobot.MomoBot;
import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IInviteTrigger;

/**
 * @author mauhiz
 */
public class JoinOnInviteTrigger extends AbstractTrigger implements IInviteTrigger {
    /**
     * @param trigger
     */
    public JoinOnInviteTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * On est sympa, on join tout.
     * 
     * @see ircbot.trigger.IInviteTrigger#executeInviteTrigger(ircbot.Channel, ircbot.IrcUser)
     */
    public void executeInviteTrigger(final Channel toChannel, final IrcUser invitedBy) {
        MomoBot.getBotInstance().joinChannel(toChannel.toString());
        MomoBot.getBotInstance().sendNotice(invitedBy, "Allez, c'est bien parce que c'est toi.");
    }
}
