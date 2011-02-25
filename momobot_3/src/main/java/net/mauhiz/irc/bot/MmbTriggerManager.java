package net.mauhiz.irc.bot;

import java.util.Iterator;

import net.mauhiz.irc.base.trigger.DefaultTriggerManager;
import net.mauhiz.irc.base.trigger.ITrigger;
import net.mauhiz.irc.bot.triggers.ICommand;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class MmbTriggerManager extends DefaultTriggerManager {

    private static final Logger LOG = Logger.getLogger(MmbTriggerManager.class);

    /**
     * @param className
     * @param texts
     */
    public void removeTrigger(String className, String... texts) {
        try {
            Class<?> toRemove = Class.forName(className);
            if (!ITrigger.class.isAssignableFrom(toRemove)) {
                return;
            }
            for (Iterator<ITrigger> trigIt = getTriggers().iterator(); trigIt.hasNext();) {
                ITrigger every = trigIt.next();

                if (toRemove.equals(every.getClass())) {
                    // remove only specified triggers

                    if (every instanceof ICommand && !ArrayUtils.isEmpty(texts)) {
                        ICommand textTrigger = (ICommand) every;
                        for (String text : texts) {
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
