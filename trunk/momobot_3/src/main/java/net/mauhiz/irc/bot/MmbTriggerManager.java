package net.mauhiz.irc.bot;

import java.lang.reflect.InvocationTargetException;

import net.mauhiz.irc.base.ITriggerManager;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Invite;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.IInviteTrigger;
import net.mauhiz.irc.bot.triggers.IJoinTrigger;
import net.mauhiz.irc.bot.triggers.INoticeTrigger;
import net.mauhiz.irc.bot.triggers.IPartTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class MmbTriggerManager implements ITriggerManager {
    /**
     * logger
     */
    private static final Logger LOG = Logger.getLogger(MmbTriggerManager.class);
    TriggerKeeper myKeeper = new TriggerKeeper();
    /**
     * @param triggerClass
     * @param params
     */
    public void addTrigger(final Class<ITrigger> triggerClass, final Object... params) {
        try {
            ITrigger trigger = (ITrigger) ConstructorUtils.invokeConstructor(triggerClass, params);
            myKeeper.add(trigger);
        } catch (InstantiationException e) {
            LOG.warn(e, e);
        } catch (IllegalAccessException e) {
            LOG.warn(e, e);
        } catch (InvocationTargetException e) {
            LOG.warn(e, e);
        } catch (NoSuchMethodException e) {
            LOG.warn(e, e);
        }
    }
    
    /**
     * @return {@link #myKeeper}
     */
    public TriggerKeeper getKeeper() {
        return myKeeper;
    }
    
    /**
     * @see net.mauhiz.irc.base.ITriggerManager#processMsg(net.mauhiz.irc.base.msg.IIrcMessage,
     *      net.mauhiz.irc.base.IrcControl)
     */
    @Override
    public void processMsg(final IIrcMessage msg, final IrcControl control) {
        if (msg == null) {
            LOG.warn("received null msg");
            return;
        }
        LOG.debug("received " + msg.getClass().getSimpleName() + ": " + msg);
        try {
            synchronized (myKeeper) {
                for (ITrigger trigger : myKeeper.getTriggers()) {
                    if (msg instanceof Privmsg && trigger instanceof IPrivmsgTrigger) {
                        IPrivmsgTrigger trig = (IPrivmsgTrigger) trigger;
                        Privmsg priv = (Privmsg) msg;
                        if (trig.isActivatedBy(priv.getMessage())) {
                            trig.doTrigger(priv, control);
                        }
                    } else if (msg instanceof Notice && trigger instanceof INoticeTrigger) {
                        INoticeTrigger trig = (INoticeTrigger) trigger;
                        Notice notic = (Notice) msg;
                        if (trig.isActivatedBy(notic.getMessage())) {
                            trig.doTrigger(notic, control);
                        }
                    } else if (msg instanceof Join && trigger instanceof IJoinTrigger) {
                        ((IJoinTrigger) trigger).doTrigger((Join) msg, control);
                    } else if (msg instanceof Part && trigger instanceof IPartTrigger) {
                        ((IPartTrigger) trigger).doTrigger((Part) msg, control);
                    } else if (msg instanceof Invite && trigger instanceof IInviteTrigger) {
                        ((IInviteTrigger) trigger).doTrigger((Invite) msg, control);
                    }
                }
            }
        } catch (RuntimeException unexpected) {
            LOG.error(unexpected, unexpected);
        }
    }
}
