package ircbot.trigger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractTrigger implements ITrigger {
    /**
     * 
     */
    private static final Logger        LOG      = Logger.getLogger(AbstractTrigger.class);
    /**
     * tous les triggers qui existent.
     */
    private static final Set<ITrigger> TRIGGERS = new HashSet<ITrigger>();

    /**
     * efface tous les triggers.
     */
    public static void clearTriggers() {
        TRIGGERS.clear();
    }

    /**
     * @return un iterateur sur les triggers
     */
    public static Collection<ITrigger> getTriggers() {
        return TRIGGERS;
    }
    /**
     * si le trigger est actif.
     */
    private boolean      actif;
    /**
     * le texte du trigger.
     */
    private final String triggerText;

    /**
     * TODO n'ajouter que si il n'y a aucune instance dans le set.
     * 
     * @param trigger
     *            le trigger
     */
    public AbstractTrigger(final String trigger) {
        this.triggerText = trigger;
        TRIGGERS.add(this);
    }

    /**
     * @param message
     *            le message
     * @return les params du msg
     */
    public final String getArgs(final String message) {
        return StringUtils.substringAfter(message, this.triggerText).trim();
    }

    /**
     * @return Returns the triggerText.
     */
    public final String getTriggerText() {
        return this.triggerText;
    }

    /**
     * @return le actif
     */
    protected final boolean isActif() {
        return this.actif;
    }

    /**
     * Méthode overridable.
     * 
     * @param msg
     * @return si le test est concluant.
     */
    public boolean isActivatedBy(final String msg) {
        boolean activated = msg.toLowerCase(Locale.FRANCE).startsWith(getTriggerText());
        if (activated) {
            LOG.debug("Trigger " + getClass() + " activated");
        }
        return activated;
    }

    /**
     * @param actif1
     *            le actif à régler
     */
    protected final void setActif(final boolean actif1) {
        this.actif = actif1;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getTriggerText();
    }
}
