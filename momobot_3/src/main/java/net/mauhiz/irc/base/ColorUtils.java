package net.mauhiz.irc.base;

import org.apache.commons.lang.text.StrBuilder;

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
 * 
 * @author Paul James Mutton, <a
 *         href="http://www.jibble.org/">http://www.jibble.org/</a>
 */
public class ColorUtils implements IrcSpecialChars {
    /**
     * Removes all colours from a line of IRC text.
     * 
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
     * 
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
     * 
     * @since PircBot 1.2.0
     * @param line
     *            the input text.
     * @return the same text, but without formatting and colour characters.
     */
    public static String removeFormattingAndColors(final String line) {
        return removeFormatting(removeColors(line));
    }
    
    /**
     * @param text
     *            la chaine
     * @param frontColor
     *            la couleur du texte
     * @param backColor
     *            la couleur du fond
     * @return la chaine coloree
     */
    public static String toBiColor(final String text, final Color frontColor, final Color backColor) {
        return COLOR + frontColor.getCode() + ',' + backColor.getCode() + text + NORMAL;
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
     * @param text
     *            la chaine à colorier
     * @param color
     *            la couleur
     * @return la chaine coloree
     */
    public static String toColor(final String text, final Color color) {
        return COLOR + color.getCode() + text + NORMAL;
    }
    
    /**
     * constructeur par défaut.
     */
    protected ColorUtils() {
        throw new UnsupportedOperationException();
    }
}
