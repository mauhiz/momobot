package ircbot;

import org.apache.commons.lang.StringUtils;

/**
 * The Colors class provides several static fields and methods that you may find
 * useful when writing an IRC Bot.
 * <p>
 * This class contains constants that are useful for formatting lines sent to
 * IRC servers. These constants allow you to apply various formatting to the
 * lines, such as colours, boldness, underlining and reverse text.
 * <p>
 * The class contains static methods to remove colours and formatting from lines
 * of IRC text.
 * <p>
 * Please note that some IRC channels may be configured to reject any messages
 * that use colours. Also note that older IRC clients may be unable to correctly
 * display lines that contain colours and other control characters.
 * <p>
 * Note that this class name has been spelt in the American style in order to
 * remain consistent with the rest of the Java API.
 * @since 0.9.12
 * @author Paul James Mutton, <a
 *         href="http://www.jibble.org/">http://www.jibble.org/</a>
 * @version 1.4.4 (Build time: Tue Mar 29 20:58:46 2005)
 */
public abstract class AColors implements IIrcSpecialChars {
    /**
     * Black coloured text.
     */
    public static final String BLACK      = "01";
    /**
     * Blue coloured text.
     */
    public static final String BLUE       = "12";
    /**
     * Brown coloured text.
     */
    public static final String BROWN      = "05";
    /**
     * Cyan coloured text.
     */
    public static final String CYAN       = "11";
    /**
     * Dark blue coloured text.
     */
    public static final String DARK_BLUE  = "02";
    /**
     * Dark gray coloured text.
     */
    public static final String DARK_GRAY  = "14";
    /**
     * Dark green coloured text.
     */
    public static final String DARK_GREEN = "03";
    /**
     * Green coloured text.
     */
    public static final String GREEN      = "09";
    /**
     * Light gray coloured text.
     */
    public static final String LIGHT_GRAY = "15";
    /**
     * Magenta coloured text.
     */
    public static final String MAGENTA    = "13";
    /**
     * Olive coloured text.
     */
    public static final String OLIVE      = "07";
    /**
     * Purple coloured text.
     */
    public static final String PURPLE     = "06";
    /**
     * Red coloured text.
     */
    public static final String RED        = "04";
    /**
     * Teal coloured text.
     */
    public static final String TEAL       = "10";
    /**
     * White coloured text.
     */
    public static final String WHITE      = "00";
    /**
     * Yellow coloured text.
     */
    public static final String YELLOW     = "08";

    /**
     * Removes all colours from a line of IRC text.
     * @since PircBot 1.2.0
     * @param line
     *            the input text.
     * @return the same text, but with all colours removed.
     */
    public static String removeColors(final String line) {
        final int length = line.length();
        final StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (i < length) {
            char ch = line.charAt(i);
            if (ch == NORMAL) {
                i++;
                continue;
            } else if (ch == COLOR) {
                if (++i >= length) {
                    break;
                }
                // Skip "x" or "xy" (foreground color).
                ch = line.charAt(i);
                if (Character.isDigit(ch)) {
                    if (++i >= length) {
                        break;
                    }
                    ch = line.charAt(i);
                    if (Character.isDigit(ch)) {
                        i++;
                    }
                    // Now skip ",x" or ",xy" (background color).
                    if (i >= length) {
                        break;
                    }
                    ch = line.charAt(i);
                    if (ch == ',') {
                        if (++i >= length) {
                            // Keep the comma.
                            i--;
                            continue;
                        }
                        ch = line.charAt(i);
                        if (Character.isDigit(ch)) {
                            if (++i >= length) {
                                break;
                            }
                            ch = line.charAt(i);
                            if (Character.isDigit(ch)) {
                                i++;
                            }
                            continue;
                        }
                        // Keep the comma.
                        i--;
                        continue;
                    }
                }
                continue;
            }
            buffer.append(ch);
            i++;
        }
        return buffer.toString();
    }

    /**
     * Remove formatting from a line of IRC text.
     * @since PircBot 1.2.0
     * @param line
     *            the input text.
     * @return the same text, but without any bold, underlining, reverse, etc.
     */
    public static String removeFormatting(final String line) {
        String temp = line;
        temp = StringUtils.remove(temp, NORMAL);
        temp = StringUtils.remove(temp, BOLD);
        temp = StringUtils.remove(temp, UNDERLINE);
        temp = StringUtils.remove(temp, REVERSE);
        return temp;
    }

    /**
     * Removes all formatting and colours from a line of IRC text.
     * @since PircBot 1.2.0
     * @param line
     *            the input text.
     * @return the same text, but without formatting and colour characters.
     */
    public static String removeFormattingAndColors(final String line) {
        return removeFormatting(removeColors(line));
    }

    /**
     * @param string
     *            la chaine
     * @param frontColor
     *            la couleur du texte
     * @param backColor
     *            la couleur du fond
     * @return la chaine coloree
     */
    public static String toBiColor(final String string,
            final String frontColor, final String backColor) {
        return COLOR + frontColor + ',' + backColor + string + NORMAL;
    }

    /**
     * @param s
     *            la chaine à graisser (LOL)
     * @return la string en gras
     */
    public static String toBold(final String s) {
        return BOLD + s + NORMAL;
    }

    /**
     * @param string
     *            la chaine à colorier
     * @param color
     *            la couleur
     * @return la chaine coloree
     */
    public static String toColor(final String string, final String color) {
        return COLOR + color + string + NORMAL;
    }

    /**
     * constructeur par défaut.
     */
    protected AColors() {
        throw new UnsupportedOperationException();
    }
}
