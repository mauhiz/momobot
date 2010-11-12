package net.mauhiz.irc.bot;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.ITriggerManager;
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
import net.mauhiz.irc.bot.triggers.ITextTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;
import net.mauhiz.util.AbstractRunnable;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class MmbTriggerManager implements ITriggerManager {
    /**
     * @author mauhiz
     * 
     */
    class TriggerLoop extends AbstractRunnable {
        private final IIrcControl control;
        private final IIrcMessage msg;
        
        /**
         * @param control1
         * @param msg1
         */
        public TriggerLoop(IIrcMessage msg1, IIrcControl control1) {
            super();
            control = control1;
            msg = msg1;
        }
        
        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
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
    static final Logger LOG = Logger.getLogger(MmbTriggerManager.class);
    
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
     * @return une vue (read-only) des triggers
     */
    public Iterable<ITrigger> getTriggers() {
        return myKeeper;
    }
    
    /**
     * @param trigClassFull
     * @param prefix
     * @param trigTexts
     */
    public void loadTrigClass(String trigClassFull, String prefix, String[] trigTexts) {
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
        
        if (ArrayUtils.isEmpty(trigTexts)) {
            LOG.debug("loading trigger: " + trigClass.getSimpleName());
            addTrigger(trigClass, (Object[]) null);
        } else {
            for (String trigText : trigTexts) {
                if (prefix != null) {
                    trigText = prefix + trigText;
                }
                addTrigger(trigClass, trigText);
                LOG.debug("loading trigger with command '" + trigText + "': " + trigClass.getSimpleName());
            }
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.ITriggerManager#processMsg(IIrcMessage, IIrcControl)
     */
    @Override
    public void processMsg(IIrcMessage msg, IIrcControl control) {
        if (msg == null) {
            LOG.warn("received null msg");
            return;
        }
        LOG.debug("received " + msg.getClass().getSimpleName() + ": " + msg);
        TriggerExecutor.getInstance().execute(new TriggerLoop(msg, control));
    }
    
    /**
     * @param className
     * @param texts
     */
    public void removeTrigger(String className, String[] texts) {
        try {
            Class<?> toRemove = Class.forName(className);
            if (!ITrigger.class.isAssignableFrom(toRemove)) {
                return;
            }
            for (ITrigger every : myKeeper) {
                if (toRemove.equals(every.getClass())) {
                    if (every instanceof ITextTrigger && !ArrayUtils.isEmpty(texts)) {
                        ITextTrigger textTrigger = (ITextTrigger) every;
                        for (String text : texts) {
                            if (textTrigger.getTriggerText().equals(text)) {
                                myKeeper.remove(every);
                            }
                        }
                    }
                    /* degage */
                    myKeeper.remove(every);
                }
            }
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.ITriggerManager#shutdown()
     */
    @Override
    public void shutdown() {
        TriggerExecutor.getInstance().shutdown();
    }
}
