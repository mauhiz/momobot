package net.mauhiz.irc.base;

/**
 * @author mauhiz
 */
public interface IrcSpecialChars {
    /**
     * .
     */
    char BELL = 0x7;
    /**
     * .
     */
    char CHAN_DEFAULT = '#';
    /**
     * .
     */
    char CHAN_LOCAL = '&';
    /**
     * .
     */
    char COLON = ':';
    /**
     * Bold text.
     */
    char DELIM_BOLD = 0x2;
    /**
     * Colored text.
     */
    char DELIM_COLOR = 3;
    /**
     * Removes all previously applied color and formatting attributes.
     */
    char DELIM_NORMAL = 0xf;
    /**
     * Reversed text (may be rendered as italic text in some clients).
     */
    char DELIM_REVERSE = 0x16;
    /**
     * Underlined text.
     */
    char DELIM_UNDERLINE = 0x1f;
    /**
     * DLE.
     */
    char M_QUOTE = 0x10;
    /**
     * arobase.
     */
    char MASK_AROBAZ = '@';
    /**
     * !.
     */
    char MASK_EXCL = '!';
    /**
     * desactivation 'un mode.
     */
    char MODE_MINUS = '-';
    /**
     * activation d'un mode.
     */
    char MODE_PLUS = '+';
    /**
     * half-op.
     */
    char PREFIX_HALFOP = '%';
    /**
     * op.
     */
    char PREFIX_OP = '@';
    /**
     * voice.
     */
    char PREFIX_VOICE = '+';
    /**
     * .
     */
    char QUOTE_BACKSLASH = '\\';
    /**
     * .
     */
    char QUOTE_CR = '\r';
    /**
     * .
     */
    char QUOTE_LF = '\n';
    /**
     * .
     */
    char QUOTE_NUL = 0;
    /**
     * .
     */
    char QUOTE_SPC = ' ';
    /**
     * delimiteur CTCP
     */
    char QUOTE_STX = 1;
    /**
     * .
     */
    char[] WHITE = new char[]{QUOTE_NUL, QUOTE_LF, QUOTE_CR, QUOTE_SPC};
    /**
     * .
     */
    char[] Z_NOTCHSTRING = new char[]{QUOTE_NUL, BELL, QUOTE_LF, QUOTE_CR, QUOTE_SPC, ','};
    /**
     * .
     */
    char[] Z_SPCRLFCL = new char[]{QUOTE_NUL, QUOTE_CR, QUOTE_LF, QUOTE_SPC, COLON};
    /**
     * .
     */
    char[] Z_SPECIAL = new char[]{'-', '[', ']', QUOTE_BACKSLASH, '`', '^', '{', '}'};
}
