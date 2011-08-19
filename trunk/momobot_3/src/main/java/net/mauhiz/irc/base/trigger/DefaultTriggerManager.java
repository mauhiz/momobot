package net.mauhiz.irc.base.trigger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Invite;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.NamedRunnable;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class DefaultTriggerManager implements ITriggerManager {
    /**
     * @author mauhiz
     * 
     */
    class TriggerLoop extends NamedRunnable {
        private final IIrcControl control;
        private final IIrcMessage msg;

        /**
         * @param control1
         * @param msg1
         */
        public TriggerLoop(IIrcMessage msg1, IIrcControl control1) {
            super("Trigger Loop");
            control = control1;
            msg = msg1;
        }

        @Override
        protected ExecutionType getExecutionType() {
            return ExecutionType.PARALLEL_CACHED;
        }

        @Override
        public void trun() {
            try {
                for (ITrigger trigger : getTriggers()) {
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
                    } else if (msg instanceof Kick && trigger instanceof IKickTrigger) {
                        ((IKickTrigger) trigger).doTrigger((Kick) msg, control);
                    }
                }

            } catch (RuntimeException unexpected) {
                LOG.error(unexpected, unexpected);
            }
        }
    }

    /**
     * logger
     */
    static final Logger LOG = Logger.getLogger(DefaultTriggerManager.class);

    /**
     * keeper of the seven keys
     */
    private final Set<ITrigger> myKeeper = new CopyOnWriteArraySet<ITrigger>();

    /**
     * @param triggerClass
     * @param params
     */
    void addTrigger(Class<? extends ITrigger> triggerClass, Object... params) {
        try {
            ITrigger trigger = (ITrigger) ConstructorUtils.invokeConstructor(triggerClass, params);
            addTrigger(trigger);
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

    public void addTrigger(ITrigger trigger) {
        if (!(trigger instanceof ITextTrigger)) {
            /* seuls les triggers text peuvent avoir plusieurs instances */
            for (ITrigger every : myKeeper) {
                if (trigger.getClass().equals(every.getClass())) {
                    /* refused */
                    return;
                }
            }
        }
        myKeeper.add(trigger);
    }

    /**
     * @return une vue (read-only) des triggers
     */
    public Iterable<ITrigger> getTriggers() {
        return myKeeper;
    }

    /**
     * @param trigClassFull
     * @param prefix
     * @param args
     */
    public void loadTrigClass(String trigClassFull, String prefix, Collection<String> args) {
        Class<? extends ITrigger> trigClass;
        try {
            Class<?> wannabe = Class.forName(trigClassFull);
            if (!ITrigger.class.isAssignableFrom(wannabe)) {
                LOG.warn("Not a trigger: " + wannabe.getName());
                return;
            }
            trigClass = wannabe.asSubclass(ITrigger.class);
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
            return;
        }

        if (args.isEmpty()) {
            LOG.debug("loading trigger: " + trigClass.getSimpleName());
            addTrigger(trigClass, (Object[]) null);
        } else {
            for (String trigText : args) {
                if (prefix != null) {
                    trigText = prefix + trigText;
                }
                addTrigger(trigClass, trigText);
                LOG.debug("loading trigger with command '" + trigText + "': " + trigClass.getSimpleName());
            }
        }
    }

    /**
     * @see net.mauhiz.irc.base.trigger.ITriggerManager#processMsg(IIrcMessage, IIrcControl)
     */
    public boolean processMsg(IIrcMessage msg, IIrcControl control) {
        if (msg == null) {
            LOG.warn("received null msg");
            return true;
        }
        LOG.debug("received " + msg.getClass().getSimpleName() + ": " + msg);
        new TriggerLoop(msg, control).launch(null);
        return false;
    }
}