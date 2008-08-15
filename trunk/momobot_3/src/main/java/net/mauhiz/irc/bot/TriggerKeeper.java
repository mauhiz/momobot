package net.mauhiz.irc.bot;

import java.util.HashSet;

import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author mauhiz
 */
public class TriggerKeeper extends HashSet<ITrigger> {
    /**
     * serial.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @see java.util.HashSet#add(java.lang.Object)
     */
    @Override
    public boolean add(final ITrigger e) {
        if (e == null) {
            return false;
        }
        if (!(e instanceof AbstractTextTrigger)) {
            /* seuls les triggers text peuvent avoir plusieurs instances */
            Class<? extends ITrigger> wannaEnter = e.getClass();
            for (ITrigger every : this) {
                if (wannaEnter.equals(every.getClass())) {
                    /* refused */
                    return false;
                }
            }
        }
        return super.add(e);
    }
    
    /**
     * @param toRemove
     * @param texts
     * @return {@link #remove(Object)}
     */
    public boolean remove(final Class<? extends ITrigger> toRemove, final String[] texts) {
        for (ITrigger every : this) {
            if (toRemove.equals(every.getClass())) {
                if (every instanceof AbstractTextTrigger && !ArrayUtils.isEmpty(texts)) {
                    AbstractTextTrigger textTrigger = (AbstractTextTrigger) every;
                    boolean atLeastOne = false;
                    for (String text : texts) {
                        if (textTrigger.getTriggerText().equals(text)) {
                            atLeastOne |= super.remove(every);
                        }
                    }
                    return atLeastOne;
                }
                /* degage */
                return super.remove(every);
            }
        }
        return false;
    }
}
