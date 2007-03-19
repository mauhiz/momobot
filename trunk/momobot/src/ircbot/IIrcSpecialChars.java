package ircbot;

/**
 * @author viper
 */
public interface IIrcSpecialChars {
    /**
     * .
     */
    char   BACKSLASH     = '\\';

    /**
     * .
     */
    char   BELL          = '\u0007';

    /**
     * Bold text.
     */
    char   BOLD          = '\u0002';

    /**
     * .
     */
    char   CHAN_DEFAULT  = '#';

    /**
     * .
     */
    char   CHAN_LOCAL    = '&';

    /**
     * .
     */
    char   COLON         = ':';

    /**
     * Colored text.
     */
    char   COLOR         = '\u0003';

    /**
     * .
     */
    char   CR            = '\r';

    /**
     * .
     */
    char   LF            = '\n';

    /**
     * DLE.
     */
    char   M_QUOTE       = '\u0010';

    /**
     * .
     */
    char   MINUS         = '-';

    /**
     * Removes all previously applied color and formatting attributes.
     */
    char   NORMAL        = '\u000f';

    /**
     * .
     */
    char   NUL           = '\0';

    /**
     * .
     */
    char   PLUS          = '+';

    /**
     * op.
     */
    char   PREFIX_OP     = '@';

    /**
     * voice.
     */
    char   PREFIX_VOICE  = '+';

    /**
     * Reversed text (may be rendered as italic text in some clients).
     */
    char   REVERSE       = '\u0016';

    /**
     * .
     */
    char   SPC           = ' ';

    /**
     * .
     */
    char   STX           = '\1';

    /**
     * Underlined text.
     */
    char   UNDERLINE     = '\u001f';

    /**
     * .
     */
    char[] WHITE         = new char[] {
            SPC, NUL, CR, LF
                         };

    /**
     * .
     */
    char[] Z_NOTCHSTRING = new char[] {
            SPC, BELL, NUL, CR, LF, ','
                         };

    /**
     * .
     */
    char[] Z_SPCRLFCL    = new char[] {
            NUL, CR, LF, SPC, COLON
                         };

    /**
     * .
     */
    char[] Z_SPECIAL     = new char[] {
            '-', '[', ']', BACKSLASH, '`', '^', '{', '}'
                         };
}
