package net.mauhiz.irc.base;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.lang3.text.StrBuilder;

/**
 * @author mauhiz
 */
public final class ColorUtils implements IrcSpecialChars {
    private static String getCloseTag(String tagName) {
        return "</" + tagName + ">";
    }

    private static String getOpenTag(String tagName, Map<String, String> attrs) {
        if (attrs == null) {
            return "<" + tagName + ">";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<" + tagName);
        for (Entry<String, String> att : attrs.entrySet()) {
            sb.append(" " + att.getKey() + "=\"" + att.getValue() + '"');
        }
        return sb.toString();
    }

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
     * @param line
     *            the input text.
     * @return the same text, but without any bold, underlining, reverse, etc.
     */
    public static String removeFormatting(String line) {
        return new StrBuilder(line).deleteAll(DELIM_NORMAL).deleteAll(DELIM_BOLD).deleteAll(DELIM_UNDERLINE)
                .deleteAll(DELIM_REVERSE).toString();
    }

    /**
     * Removes all formatting and colours from a line of IRC text.
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

    public static String toHTML(String text) {
        StringBuilder result = new StringBuilder();
        Stack<String> openTags = new Stack<String>();
        for (int i = 0; i < text.length(); i++) {
            char next = text.charAt(i);
            if (next == DELIM_COLOR) {
                if ("font".equals(openTags.peek())) {
                    result.append(getCloseTag("font"));
                    continue;
                }
                char fgcolor1 = text.charAt(i + 1);
                if (!Character.isDigit(fgcolor1)) {
                    continue;
                }
                i++; // consuming
                char fgcolor2 = text.charAt(i + 1);
                String fgColorStr;
                if (Character.isDigit(fgcolor2)) {
                    i++; // consuming
                    fgColorStr = new String(new char[] { fgcolor1, fgcolor2 });
                } else {
                    fgColorStr = Character.toString(fgcolor1);
                }
                Color fgcolor = Color.fromCode(fgColorStr);
                Color bgcolor = null;
                if (text.charAt(i + 1) == ',') {
                    char bgcolor1 = text.charAt(i + 2);
                    if (Character.isDigit(bgcolor1)) {
                        i += 2;
                        char bgcolor2 = text.charAt(i + 1);
                        String bgColorStr;
                        if (Character.isDigit(bgcolor2)) {
                            i++; // consuming
                            bgColorStr = new String(new char[] { bgcolor1, bgcolor2 });
                        } else {
                            bgColorStr = Character.toString(bgcolor1);
                        }
                        bgcolor = Color.fromCode(bgColorStr);
                    }
                }
                Map<String, String> attributes = Collections.singletonMap("style", "color: " + fgcolor.getCssName()
                        + ";" + (bgcolor == null ? "" : "background-color: " + bgcolor.getCssName()));
                openTags.add("font");
                result.append(getOpenTag("font", attributes));
                continue;
            } else if (next == DELIM_BOLD) {
                if ("b".equals(openTags.peek())) {
                    result.append(getCloseTag("b"));
                    continue;
                }
                result.append(getOpenTag("b", null));
                continue;
            } else if (next == DELIM_REVERSE) {
                if ("i".equals(openTags.peek())) {
                    result.append(getCloseTag("i"));
                    continue;
                }
                result.append(getOpenTag("i", null));
                continue;
            } else if (next == DELIM_UNDERLINE) {
                if ("u".equals(openTags.peek())) {
                    result.append(getCloseTag("u"));
                    continue;
                }
                result.append(getOpenTag("u", null));
                continue;

            } else if (next == DELIM_NORMAL) {
                while (!openTags.isEmpty()) {
                    result.append(getCloseTag(openTags.pop()));
                }
            } else {
                result.append(next);
            }
        }
        while (!openTags.isEmpty()) {
            result.append(getCloseTag(openTags.pop()));
        }
        return result.toString();
    }

    /**
     * @return la string en italique
     */
    public static String toItalic(String string) {
        return DELIM_REVERSE + string + DELIM_REVERSE;
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
