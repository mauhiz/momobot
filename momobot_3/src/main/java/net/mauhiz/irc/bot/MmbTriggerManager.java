package net.mauhiz.irc.bot;

import java.util.Collection;
import java.util.Iterator;

import net.mauhiz.irc.base.trigger.DefaultTriggerManager;
import net.mauhiz.irc.base.trigger.ITrigger;
import net.mauhiz.irc.bot.triggers.ICommand;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class MmbTriggerManager extends DefaultTriggerManager {

    private static final Logger LOG = Logger.getLogger(MmbTriggerManager.class);

    /**
     * @param className
     * @param args
     */
    public void removeTrigger(String className, Collection<String> args) {
        try {
            Class<?> toRemove = Class.forName(className);
            if (!ITrigger.class.isAssignableFrom(toRemove)) {
                return;
            }
            for (Iterator<ITrigger> trigIt = getTriggers().iterator(); trigIt.hasNext();) {
                ITrigger every = trigIt.next();

                if (toRemove.equals(every.getClass())) {
                    // remove only specified triggers

                    if (every instanceof ICommand && args != null && !args.isEmpty()) {
                        ICommand textTrigger = (ICommand) every;
                        for (String text : args) {
                            if (textTrigger.getTriggerText().equals(text)) {
                                trigIt.remove();
                            }
                        }
                    } else {
                        /* degage */
                        trigIt.remove();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
        }
    }
}
