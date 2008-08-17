package net.mauhiz.irc.base;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public enum Color {
    BLACK(0x1), BLUE(0xc), BROWN(0x5), CYAN(0xb), DARK_BLUE(0x2), DARK_GRAY(0xe), DARK_GREEN(0x3), GREEN(0x9), LIGHT_GRAY(
            0xf), MAGENTA(0xd), OLIVE(0x7), PURPLE(0x6), RED(0x4), TEAL(0xa), WHITE(0x0), YELLOW(0x8);
    
    /**
     * code couleur mIRC.
     */
    private String code;
    
    /**
     * @param intCode
     */
    private Color(final int intCode) {
        code = StringUtils.leftPad(Integer.toString(intCode), 2, '0');
    }
    
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return code;
    }
}
