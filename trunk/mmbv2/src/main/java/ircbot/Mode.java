package ircbot;

import static org.apache.commons.lang.StringUtils.contains;

/**
 * @author mauhiz
 */
public class Mode implements IIrcSpecialChars {
    /**
     * ban.
     */
    public static final char BAN             = 'b';
    /**
     * .
     */
    public static final char INVITE_ONLY     = 'i';
    /**
     * .
     */
    public static final char KEY             = 'k';
    /**
     * .
     */
    public static final char LIMIT           = 'l';
    /**
     * .
     */
    public static final char MODERATED       = 'm';
    /**
     * .
     */
    public static final char NO_EXT_MSG      = 'n';
    /**
     * op.
     */
    public static final char OP              = 'o';
    /**
     * .
     */
    public static final char PRIVATE         = 'p';
    /**
     * .
     */
    public static final char SECRET          = 's';
    /**
     * .
     */
    public static final char TOPIC_PROTECTED = 't';
    /**
     * voice.
     */
    public static final char VOICE           = 'v';
    /**
     * si je suis un op.
     */
    private boolean          op;
    /**
     * si je suis un voice.
     */
    private boolean          voice;

    /**
     * @param string
     *            la chaine de caractere du mode
     */
    public Mode(final String string) {
        this.op = contains(string, PREFIX_OP);
        this.voice = contains(string, PREFIX_VOICE);
    }

    /**
     * donne l'op.
     */
    public final void giveOp() {
        this.op = true;
    }

    /**
     * donne le voice.
     */
    public final void giveVoice() {
        this.voice = true;
    }

    /**
     * @return si ya un op
     */
    public final boolean isOp() {
        return this.op;
    }

    /**
     * @return si ya un voice
     */
    public final boolean isVoice() {
        return this.voice;
    }

    /**
     * retire l'op.
     */
    public final void removeOp() {
        this.op = false;
    }

    /**
     * retire le voice.
     */
    public final void removeVoice() {
        this.voice = false;
    }
}
