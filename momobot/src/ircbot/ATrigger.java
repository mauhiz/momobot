package ircbot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import utils.Utils;

/**
 * @author Administrator
 */
public abstract class ATrigger {
    /**
     * tous les triggers qui existent.
     */
    private static final Collection < ATrigger > TRIGGERS;
    static {
        TRIGGERS = Collections.synchronizedSet(new HashSet < ATrigger >());
    }

    /**
     * efface tous les triggers.
     */
    public static void clearTriggers() {
        TRIGGERS.clear();
    }

    /**
     * @return un iterateur sur les triggers
     */
    public static Iterator < ATrigger > getTriggers() {
        return TRIGGERS.iterator();
    }
    /**
     * si le trigger est actif.
     */
    private boolean actif       = false;
    /**
     * si il réagit en notice.
     */
    private boolean notice      = false;
    /**
     * si il faut etre admin.
     */
    private boolean onlyAdmin   = false;
    /**
     * si il réagit en privé.
     */
    private boolean prive       = false;
    /**
     * si il réagit en public.
     */
    private boolean publique    = false;
    /**
     * le texte du trigger.
     */
    private String  triggerText = "";

    /**
     * @param trigger
     *            le trigger
     */
    public ATrigger(final String trigger) {
        this.triggerText = trigger;
        TRIGGERS.add(this);
    }

    /**
     * @param user
     *            le user
     * @param message
     *            le message
     */
    public abstract void executePrivateTrigger(IrcUser user, String message);

    /**
     * @param user
     *            le user
     * @param channel
     *            le channel
     * @param message
     *            le message
     */
    public abstract void executePublicTrigger(IrcUser user, String channel,
            String message);

    /**
     * @param message
     *            le message
     * @return les params du msg
     */
    public final String getArgs(final String message) {
        final int l = this.triggerText.length();
        return message.substring(l).trim();
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
     * @return Returns the onlyAdmin.
     */
    public final boolean isOnlyAdmin() {
        return this.onlyAdmin;
    }

    /**
     * @param actif1
     *            le actif à régler
     */
    protected final void setActif(final boolean actif1) {
        this.actif = actif1;
    }

    /**
     * @param text
     *            le texte
     * @return si le trigger est déclenché en notice
     */
    public final boolean testNotice(final String text) {
        if (!isNotice()) {
            return false;
        }
        return text.toLowerCase().startsWith(this.triggerText);
    }

    /**
     * @param text
     *            le text
     * @return si le trigger est déclenché en privé
     */
    public final boolean testPrive(final String text) {
        if (!isPrive()) {
            return false;
        }
        boolean b = text.toLowerCase().startsWith(
                this.triggerText.toLowerCase());
        if (!b) {
            return false;
        }
        Utils.log(getClass(), getTriggerText());
        return true;
    }

    /**
     * @param text
     *            le texte
     * @return si le trigger est déclenché en public
     */
    public final boolean testPublic(final String text) {
        if (!isPublic()) {
            return false;
        }
        boolean b = text.toLowerCase().startsWith(
                this.triggerText.toLowerCase());
        if (!b) {
            return false;
        }
        Utils.log(getClass(), getTriggerText());
        return true;
    }

    /**
     * @param publique1
     *            un booléen
     */
    protected final void setPublic(final boolean publique1) {
        this.publique = publique1;
    }

    /**
     * @param prive1
     *            un booléen
     */
    protected final void setPrive(final boolean prive1) {
        this.prive = prive1;
    }

    /**
     * @param notice1
     *            un booléen
     */
    protected final void setNotice(final boolean notice1) {
        this.notice = notice1;
    }

    /**
     * @return this.public
     */
    public final boolean isPublic() {
        return this.publique;
    }

    /**
     * @return this.prive
     */
    public final boolean isPrive() {
        return this.prive;
    }

    /**
     * @return this.notice
     */
    public final boolean isNotice() {
        return this.notice;
    }

    /**
     * @param b
     *            un booléen
     */
    protected final void setOnlyAdmin(final boolean b) {
        this.onlyAdmin = b;
    }
}
