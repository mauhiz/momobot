package net.mauhiz.irc.base;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.util.StringUtil;
import net.mauhiz.util.UtfChar;

/**
 * @author mauhiz
 */
public enum ColorUtils implements IrcSpecialChars {
    ;
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
        for (int index = 0; index < length;) {
            UtfChar car = UtfChar.charAt(line, index);
            if (DELIM_NORMAL.equals(car)) {
                ++index;
                continue;
            } else if (DELIM_COLOR.equals(car)) {
                if (++index >= length) {
                    break;
                }
                /* Skip "x" or "xy" (foreground color). */
                car = UtfChar.charAt(line, index);
                if (car.isDigit()) {
                    if (++index >= length) {
                        break;
                    }
                    car = UtfChar.charAt(line, index);
                    if (car.isDigit()) {
                        ++index;
                    }
                    /* Now skip ",x" or ",xy" (background color). */
                    if (index >= length) {
                        break;
                    }
                    car = UtfChar.charAt(line, index);
                    if (car.isEquals(',')) {
                        if (++index >= length) {
                            /* Keep the comma. */
                            --index;
                            continue;
                        }
                        car = UtfChar.charAt(line, index);
                        if (car.isDigit()) {
                            if (++index >= length) {
                                break;
                            }
                            car = UtfChar.charAt(line, index);
                            if (car.isDigit()) {
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
        return StringUtil.remove(line, new UtfChar[] { DELIM_NORMAL, DELIM_BOLD, DELIM_UNDERLINE, DELIM_REVERSE });
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
        Deque<String> openTags = new ArrayDeque<>();
        for (int i = 0; i < text.length(); i++) {
            UtfChar next = UtfChar.charAt(text, i);
            if (next.equals(DELIM_COLOR)) {
                if ("font".equals(openTags.peek())) {
                    result.append(getCloseTag("font"));
                    continue;
                }
                UtfChar fgcolor1 = UtfChar.charAt(text, i + 1);
                if (!fgcolor1.isDigit()) {
                    continue;
                }
                i++; // consuming
                UtfChar fgcolor2 = UtfChar.charAt(text, i + 1);
                String fgColorStr;
                if (fgcolor2.isDigit()) {
                    i++; // consuming
                    fgColorStr = text.substring(i - 2, i);
                } else {
                    fgColorStr = text.substring(i - 1, i);
                }
                Color fgcolor = Color.fromCode(fgColorStr);
                Color bgcolor = null;
                if (UtfChar.charAt(text, i + 1).isEquals(',')) {
                    UtfChar bgcolor1 = UtfChar.charAt(text, i + 2);
                    if (bgcolor1.isDigit()) {
                        i += 2;
                        UtfChar bgcolor2 = UtfChar.charAt(text, i + 1);
                        String bgColorStr;
                        if (bgcolor2.isDigit()) {
                            i++; // consuming
                            bgColorStr = text.substring(i - 2, i);
                        } else {
                            bgColorStr = text.substring(i - 1, i);
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
}
