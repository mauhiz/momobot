package net.mauhiz.irc.bot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author mauhiz
 */
public class TriggerKeeper implements Iterable<ITrigger> {
    
    /**
     * serial.
     */
    private static final long serialVersionUID = 1L;
    /**
     * triggers
     */
    private final Set<ITrigger> trigs = Collections.synchronizedSet(new HashSet<ITrigger>());
    
    /**
     * @param e
     * @return {@link HashSet#add(Object)}
     */
    public boolean add(final ITrigger e) {
        if (e == null) {
            return false;
        }
        if (!(e instanceof AbstractTextTrigger)) {
            /* seuls les triggers text peuvent avoir plusieurs instances */
            Class<? extends ITrigger> wannaEnter = e.getClass();
            for (ITrigger every : trigs) {
                if (wannaEnter.equals(every.getClass())) {
                    /* refused */
                    return false;
                }
            }
        }
        return trigs.add(e);
    }
    
    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ITrigger> iterator() {
        return trigs.iterator();
    }
    
    /**
     * @param toRemove
     * @param texts
     * @return {@link HashSet#remove(Object)}
     */
    public boolean remove(final Class<? extends ITrigger> toRemove, final String[] texts) {
        for (ITrigger every : trigs) {
            if (toRemove.equals(every.getClass())) {
                if (every instanceof AbstractTextTrigger && !ArrayUtils.isEmpty(texts)) {
                    AbstractTextTrigger textTrigger = (AbstractTextTrigger) every;
                    boolean atLeastOne = false;
                    for (String text : texts) {
                        if (textTrigger.getTriggerText().equals(text)) {
                            atLeastOne |= trigs.remove(every);
                        }
                    }
                    return atLeastOne;
                }
                /* degage */
                return trigs.remove(every);
            }
        }
        return false;
    }
}
