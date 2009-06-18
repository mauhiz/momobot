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
    public static String removeColors(String line) {
        int length = line.length();
        StringBuilder buffer = new StringBuilder();
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
    public static String removeFormatting(String line) {
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
    public static String removeFormattingAndColors(String line) {
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
    public static String toBiColor(String text, Color frontColor, Color backColor) {
        return DELIM_COLOR + frontColor.toString() + ',' + backColor + text + DELIM_COLOR;
    }
    
    /**
     * @param string
     *            la chaine a graisser (LOL)
     * @return la string en gras
     */
    public static String toBold(String string) {
        return DELIM_BOLD + string + DELIM_BOLD;
    }
    
    /**
     * @param text
     *            la chaine a colorier
     * @param color
     *            la couleur
     * @return la chaine coloree
     */
    public static String toColor(String text, Color color) {
        return DELIM_COLOR + color.toString() + text + DELIM_COLOR;
    }
    
    /**
     * @param string
     *            la chaine a souligner
     * @return la string en gras
     */
    public static String toUnderline(String string) {
        return DELIM_UNDERLINE + string + DELIM_UNDERLINE;
    }
    
    /**
     * constructeur par defaut.
     */
    private ColorUtils() {
        super();
    }
}
