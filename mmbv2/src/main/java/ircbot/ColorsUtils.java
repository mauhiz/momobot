package ircbot;

import org.apache.commons.lang.text.StrBuilder;

/**
 * The Colors class provides several static fields and methods that you may find useful when writing an IRC Bot.
 * <p>
 * This class contains constants that are useful for formatting lines sent to IRC servers. These constants allow you to
 * apply various formatting to the lines, such as colours, boldness, underlining and reverse text.
 * <p>
 * The class contains static methods to remove colours and formatting from lines of IRC text.
 * <p>
 * Please note that some IRC channels may be configured to reject any messages that use colours. Also note that older
 * IRC clients may be unable to correctly display lines that contain colours and other control characters.
 * <p>
 * @author Paul James Mutton, <a href="http://www.jibble.org/">http://www.jibble.org/</a>
 */
public class ColorsUtils implements IIrcSpecialChars {
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
        final StrBuilder buffer = new StrBuilder();
        char index = 0;
        while (index < length) {
            char car = line.charAt(index);
            if (car == NORMAL) {
                ++index;
                continue;
            } else if (car == COLOR) {
                if (++index >= length) {
                    break;
                }
                /* Skip "x" or "xy" (foreground color). */
                car = line.charAt(index);
                if (Character.isDigit(car)) {
                    if (++index >= length) {
                        break;
                    }
                    car = line.charAt(index);
                    if (Character.isDigit(car)) {
                        ++index;
                    }
                    /* Now skip ",x" or ",xy" (background color). */
                    if (index >= length) {
                        break;
                    }
                    car = line.charAt(index);
                    if (car == ',') {
                        if (++index >= length) {
                            /* Keep the comma. */
                            --index;
                            continue;
                        }
                        car = line.charAt(index);
                        if (Character.isDigit(car)) {
                            if (++index >= length) {
                                break;
                            }
                            car = line.charAt(index);
                            if (Character.isDigit(car)) {
                                ++index;
                            }
                            continue;
                        }
                        /* Keep the comma. */
                        --index;
                        continue;
                    }
                }
                continue;
            }
            buffer.append(car);
            ++index;
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
        return new StrBuilder(line).deleteAll(NORMAL).deleteAll(BOLD).deleteAll(UNDERLINE).deleteAll(REVERSE)
                .toString();
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
    public static String toBiColor(final String string, final String frontColor, final String backColor) {
        return COLOR + frontColor + ',' + backColor + string + NORMAL;
    }

    /**
     * @param string
     *            la chaine à graisser (LOL)
     * @return la string en gras
     */
    public static String toBold(final String string) {
        return BOLD + string + NORMAL;
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
    protected ColorsUtils() {
        throw new UnsupportedOperationException();
    }
}
