package net.mauhiz.irc.base.trigger;

import java.util.concurrent.RecursiveAction;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Invite;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;

import org.apache.log4j.Logger;

public class TriggerTask extends RecursiveAction {

    private static final Logger LOG = Logger.getLogger(TriggerTask.class);
    private final IIrcControl control;
    private final IIrcMessage msg;
    private final ITrigger trigger;

    public TriggerTask(IIrcMessage msg, IIrcControl control, ITrigger trigger) {
        this.msg = msg;
        this.control = control;
        this.trigger = trigger;
    }

    @Override
    protected void compute() {
        try {
            tryTrigger();
        } catch (RuntimeException unexpected) {
            LOG.error(unexpected, unexpected);
        }
    }

    private void tryTrigger() {
        if (msg instanceof Privmsg && trigger instanceof IPrivmsgTrigger) {
            IPrivmsgTrigger trig = (IPrivmsgTrigger) trigger;
            Privmsg priv = (Privmsg) msg;
            if (trig.isActivatedBy(priv.getMessage())) {
                trig.doTrigger(priv, control);
            }
        } else if (msg instanceof Notice && trigger instanceof INoticeTrigger) {
            INoticeTrigger trig = (INoticeTrigger) trigger;
            Notice notice = (Notice) msg;
            if (trig.isActivatedBy(notice.getMessage())) {
                trig.doTrigger(notice, control);
            }
        } else if (msg instanceof Join && trigger instanceof IJoinTrigger) {
            ((IJoinTrigger) trigger).doTrigger((Join) msg, control);
        } else if (msg instanceof Part && trigger instanceof IPartTrigger) {
            ((IPartTrigger) trigger).doTrigger((Part) msg, control);
        } else if (msg instanceof Invite && trigger instanceof IInviteTrigger) {
            ((IInviteTrigger) trigger).doTrigger((Invite) msg, control);
        } else if (msg instanceof Kick && trigger instanceof IKickTrigger) {
            ((IKickTrigger) trigger).doTrigger((Kick) msg, control);
        }
    }
}
