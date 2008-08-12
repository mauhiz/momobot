package net.mauhiz.irc.base;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 * 
 */
public enum Color {
    BLACK(1), BLUE(12), BROWN(5), CYAN(11), DARK_BLUE(2), DARK_GRAY(14), DARK_GREEN(3), GREEN(9), LIGHT_GRAY(15), MAGENTA(
            13), OLIVE(7), PURPLE(6), RED(4), TEAL(10), WHITE(0), YELLOW(8);
    
    private String code;
    
    /**
     * @param intCode
     */
    private Color(final int intCode) {
        code = StringUtils.leftPad(Integer.toString(intCode), 2);
    }
    
    /**
     * @return {@link #code}
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return code;
    }
}
