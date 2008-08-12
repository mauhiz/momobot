package net.mauhiz.irc.bot;

import java.util.HashSet;
import java.util.Set;

import net.mauhiz.irc.bot.triggers.ITrigger;

/**
 * @author mauhiz
 */
public class TriggerKeeper {
    private final Set<ITrigger> triggers = new HashSet<ITrigger>();
    
    /**
     * @param trigger
     */
    public void add(final ITrigger trigger) {
        triggers.add(trigger);
    }
    
    /**
     * efface tous les triggers.
     */
    public void clearTriggers() {
        triggers.clear();
    }
    
    /**
     * @return un iterateur sur les triggers
     */
    public Set<ITrigger> getTriggers() {
        return triggers;
    }
}
