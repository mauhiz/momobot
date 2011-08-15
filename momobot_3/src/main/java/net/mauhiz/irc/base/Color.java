package net.mauhiz.irc.base;

import org.apache.commons.lang3.StringUtils;

/**
 * @author mauhiz
 */
public enum Color {
    BLACK(0x1), BLUE(0xc), BROWN(0x5), CYAN(0xb), DARK_BLUE(0x2), DARK_GRAY(0xe), DARK_GREEN(0x3), GREEN(0x9), LIGHT_GRAY(
            0xf), MAGENTA(0xd), OLIVE(0x7), PURPLE(0x6), RED(0x4), TEAL(0xa), WHITE(0x0), YELLOW(0x8);

    public static Color fromCode(String code) {
        for (Color color : values()) {
            if (color.code == code) {
                return color;
            }
        }

        return null;
    }

    /**
     * code couleur mIRC.
     */
    private final String code;

    /**
     * @param intCode
     */
    private Color(int intCode) {
        code = StringUtils.leftPad(Integer.toString(intCode), 2, '0');
    }

    public String getCssName() {
        return StringUtils.remove(name().toLowerCase(), "_");
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return code;
    }
}
