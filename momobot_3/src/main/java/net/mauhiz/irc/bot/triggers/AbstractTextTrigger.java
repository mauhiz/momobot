package net.mauhiz.irc.bot.triggers;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractTextTrigger implements ITextTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(AbstractTextTrigger.class);
    /**
     * si le trigger est actif.
     */
    private boolean actif;
    /**
     * le texte du trigger.
     */
    private final String triggerText;
    
    /**
     * @param trigger
     *            le trigger
     */
    public AbstractTextTrigger(final String trigger) {
        triggerText = trigger;
    }
    
    /**
     * @param message
     *            le message
     * @return les params du msg
     */
    public final String getArgs(final String message) {
        return StringUtils.substringAfter(message, triggerText).trim();
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.ITextTrigger#getTriggerHelp()
     */
    public final String getTriggerHelp() {
        return triggerText;
    }
    
    /**
     * @return le actif
     */
    protected final boolean isActif() {
        return actif;
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.ITextTrigger#isActivatedBy(java.lang.String)
     */
    public boolean isActivatedBy(final String msg) {
        boolean activated = msg.toLowerCase(Locale.FRANCE).startsWith(triggerText);
        if (activated) {
            LOG.debug("Trigger " + getClass().getSimpleName() + " activated");
        }
        return activated;
    }
    
    /**
     * @param actif1
     *            le actif à régler
     */
    protected final void setActif(final boolean actif1) {
        actif = actif1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return triggerText;
    }
}
