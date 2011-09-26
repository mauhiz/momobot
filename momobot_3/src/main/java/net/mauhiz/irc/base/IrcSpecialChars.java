package net.mauhiz.irc.base;

import java.util.Arrays;
import java.util.Collection;

import net.mauhiz.util.UtfChar;

/**
 * @author mauhiz
 */
public interface IrcSpecialChars {
    /**
     * .
     */
    UtfChar BELL = UtfChar.valueOf(0x7);
    /**
     * .
     */
    UtfChar CHAN_DEFAULT = UtfChar.valueOf('#');
    /**
     * .
     */
    UtfChar CHAN_LOCAL = UtfChar.valueOf('&');
    /**
     * .
     */
    UtfChar COLON = UtfChar.valueOf(':');
    /**
     * Bold text.
     */
    UtfChar DELIM_BOLD = UtfChar.valueOf(0x2);
    /**
     * Colored text.
     */
    UtfChar DELIM_COLOR = UtfChar.valueOf(3);
    /**
     * Removes all previously applied color and formatting attributes.
     */
    UtfChar DELIM_NORMAL = UtfChar.valueOf(0xf);
    /**
     * Reversed text (may be rendered as italic text in some clients).
     */
    UtfChar DELIM_REVERSE = UtfChar.valueOf(0x16);
    /**
     * Underlined text.
     */
    UtfChar DELIM_UNDERLINE = UtfChar.valueOf(0x1f);
    /**
     * DLE.
     */
    UtfChar M_QUOTE = UtfChar.valueOf(0x10);
    /**
     * arobase.
     */
    UtfChar MASK_AROBAZ = UtfChar.valueOf('@');
    /**
     * !.
     */
    UtfChar MASK_EXCL = UtfChar.valueOf('!');
    /**
     * desactivation 'un mode.
     */
    UtfChar MODE_MINUS = UtfChar.valueOf('-');
    /**
     * activation d'un mode.
     */
    UtfChar MODE_PLUS = UtfChar.valueOf('+');
    /**
     * half-op.
     */
    UtfChar PREFIX_HALFOP = UtfChar.valueOf('%');
    /**
     * op.
     */
    UtfChar PREFIX_OP = UtfChar.valueOf('@');
    /**
     * voice.
     */
    UtfChar PREFIX_VOICE = UtfChar.valueOf('+');
    /**
     * .
     */
    UtfChar QUOTE_BACKSLASH = UtfChar.valueOf('\\');
    /**
     * .
     */
    UtfChar QUOTE_CR = UtfChar.valueOf('\r');
    /**
     * .
     */
    UtfChar QUOTE_LF = UtfChar.valueOf('\n');
    /**
     * .
     */
    UtfChar QUOTE_NUL = UtfChar.valueOf(0);
    /**
     * .
     */
    UtfChar QUOTE_SPC = UtfChar.valueOf(' ');
    /**
     * delimiteur CTCP
     */
    UtfChar QUOTE_STX = UtfChar.valueOf(1);
    /**
     * .
     */
    Collection<UtfChar> WHITE = Arrays.asList(QUOTE_NUL, QUOTE_LF, QUOTE_CR, QUOTE_SPC);
    /**
     * .
     */
    Collection<UtfChar> Z_NOTCHSTRING = Arrays.asList(QUOTE_NUL, BELL, QUOTE_LF, QUOTE_CR, QUOTE_SPC,
            UtfChar.valueOf(','));
    /**
     * .
     */
    Collection<UtfChar> Z_SPCRLFCL = Arrays.asList(QUOTE_NUL, QUOTE_CR, QUOTE_LF, QUOTE_SPC, COLON);
    /**
     * .
     */
    Collection<UtfChar> Z_SPECIAL = Arrays.asList(UtfChar.valueOf('-'), UtfChar.valueOf('['), UtfChar.valueOf(']'),
            QUOTE_BACKSLASH, UtfChar.valueOf('`'), UtfChar.valueOf('^'), UtfChar.valueOf('{'), UtfChar.valueOf('}'));
}
