package net.mauhiz.irc.bot;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.ITriggerManager;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Invite;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IInviteTrigger;
import net.mauhiz.irc.bot.triggers.IJoinTrigger;
import net.mauhiz.irc.bot.triggers.INoticeTrigger;
import net.mauhiz.irc.bot.triggers.IPartTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;

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
    class TriggerLoop extends Thread {
        private final IIrcControl control;
        private final IIrcMessage msg;
        
        /**
         * @param control1
         * @param msg1
         */
        public TriggerLoop(final IIrcMessage msg1, final IIrcControl control1) {
            control = control1;
            msg = msg1;
        }
        
        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                for (ITrigger trigger : myKeeper) {
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
    final TriggerKeeper myKeeper = new TriggerKeeper();
    
    /**
     * @param triggerClass
     * @param params
     */
    void addTrigger(final Class<ITrigger> triggerClass, final Object... params) {
        try {
            ITrigger trigger = (ITrigger) ConstructorUtils.invokeConstructor(triggerClass, params);
            if (!(trigger instanceof AbstractTextTrigger)) {
                /* seuls les triggers text peuvent avoir plusieurs instances */
                Class<? extends ITrigger> wannaEnter = trigger.getClass();
                for (ITrigger every : myKeeper) {
                    if (wannaEnter.equals(every.getClass())) {
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
     * @return une vue des triggers
     */
    public Iterator<ITrigger> getTriggers() {
        return myKeeper.iterator();
    }
    
    /**
     * @param trigClassFull
     * @param prefix
     * @param trigTexts
     */
    public void loadTrigClass(final String trigClassFull, final String prefix, final String[] trigTexts) {
        Class<ITrigger> trigClass;
        try {
            Class<?> wannabe = Class.forName(trigClassFull);
            if (!ITrigger.class.isAssignableFrom(wannabe)) {
                LOG.warn("Not a trigger: " + wannabe.getName());
                return;
            }
            trigClass = (Class<ITrigger>) wannabe;
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
            return;
        }
        
        if (ArrayUtils.isEmpty(trigTexts)) {
            LOG.debug("loading trigger: " + trigClass.getSimpleName());
            addTrigger(trigClass, (Object[]) null);
        } else {
            for (String trigText : trigTexts) {
                String fullTrigText = prefix + trigText;
                addTrigger(trigClass, fullTrigText);
                LOG.debug("loading trigger with command '" + fullTrigText + "': " + trigClass.getSimpleName());
            }
        }
        myKeeper.update();
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
        new TriggerLoop(msg, control).start();
    }
    
    /**
     * @param className
     * @param texts
     */
    public void removeTrigger(final String className, final String[] texts) {
        try {
            Class<?> toRemove = Class.forName(className);
            if (!ITrigger.class.isAssignableFrom(toRemove)) {
                return;
            }
            for (ITrigger every : myKeeper) {
                if (toRemove.equals(every.getClass())) {
                    if (every instanceof AbstractTextTrigger && !ArrayUtils.isEmpty(texts)) {
                        AbstractTextTrigger textTrigger = (AbstractTextTrigger) every;
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
            myKeeper.update();
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
        }
    }
}
