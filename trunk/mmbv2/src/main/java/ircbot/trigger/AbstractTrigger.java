package ircbot.trigger;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public abstract class AbstractTrigger implements ITrigger {
    /**
     * 
     */
    private static boolean                       addedToSet;
    /**
     * tous les triggers qui existent.
     */
    private static final Set < AbstractTrigger > TRIGGERS = new HashSet < AbstractTrigger >();

    /**
     * efface tous les triggers.
     */
    public static void clearTriggers() {
        TRIGGERS.clear();
    }

    /**
     * @return un iterateur sur les triggers
     */
    public static Iterable < AbstractTrigger > getTriggers() {
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
     * @param trigger
     *            le trigger
     */
    public AbstractTrigger(final String trigger) {
        this.triggerText = trigger;
        if (!addedToSet) {
            synchronized (TRIGGERS) {
                TRIGGERS.add(this);
            }
            addedToSet = true;
        }
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
     * @param actif1
     *            le actif à régler
     */
    protected final void setActif(final boolean actif1) {
        this.actif = actif1;
    }

    /**
     * Méthode overridable.
     * @param msg
     * @return si le test est concluant.
     */
    public boolean test(final String msg) {
        return msg.toLowerCase(Locale.FRANCE).startsWith(getTriggerText());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getTriggerText();
    }
}
