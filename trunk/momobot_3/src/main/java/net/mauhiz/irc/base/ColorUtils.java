package net.mauhiz.irc.base;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public final class ColorUtils implements IrcSpecialChars {
    /**
     * Removes all colours from a line of IRC text.
     * 
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
            if (car == DELIM_NORMAL) {
                ++index;
                continue;
            } else if (car == DELIM_COLOR) {
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
        return new StrBuilder(line).deleteAll(DELIM_NORMAL).deleteAll(DELIM_BOLD).deleteAll(DELIM_UNDERLINE).deleteAll(
                DELIM_REVERSE).toString();
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
        return DELIM_COLOR + frontColor.toString() + ',' + backColor + text + DELIM_COLOR;
    }
    
    /**
     * @param string
     *            la chaine à graisser (LOL)
     * @return la string en gras
     */
    public static String toBold(final String string) {
        return DELIM_BOLD + string + DELIM_BOLD;
    }
    
    /**
     * @param text
     *            la chaine à colorier
     * @param color
     *            la couleur
     * @return la chaine coloree
     */
    public static String toColor(final String text, final Color color) {
        return DELIM_COLOR + color.toString() + text + DELIM_COLOR;
    }
    
    /**
     * @param string
     *            la chaine à souligner
     * @return la string en gras
     */
    public static String toUnderline(final String string) {
        return DELIM_UNDERLINE + string + DELIM_UNDERLINE;
    }
    
    /**
     * constructeur par défaut.
     */
    private ColorUtils() {
        super();
    }
}
