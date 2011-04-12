package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Invite;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.trigger.IInviteTrigger;
import net.mauhiz.util.Messages;

/**
 * @author mauhiz
 */
public class JoinOnInviteTrigger implements IInviteTrigger {

    /**
     * On est sympa, on join tout.
     * 
     * @see net.mauhiz.irc.base.trigger.IInviteTrigger#doTrigger(net.mauhiz.irc.base.msg.Invite,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Invite im, IIrcControl control) {
        Join join = new Join(im.getServer(), im.getChan());
        control.sendMsg(join);
        Notice notice = Notice.buildPrivateAnswer(im, Messages.get(getClass(), "join.on.invite")); //$NON-NLS-1$
        control.sendMsg(notice);
    }
}
