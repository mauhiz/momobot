package net.mauhiz.irc.bot.triggers.cwwb;
/**
 * 
 * @author abby
 * 
 */
public enum Level {
    GOOD(6), LOW(2), LOWPLUS(3), MID(4), MIDPLUS(5), NOOB(1), ROXOR(8), SKILLED(7), UNKOWN(0);
    
    /** L'attribut qui contient la valeur associé à l'enum */
    private final int code;
    
    /**
     * Le constructeur qui associe une valeur à l'enum
     * 
     * @param code
     */
    private Level(final int code) {
        this.code = code;
    }
    
    /**
     * @return la valeur de l'enum
     */
    public int getCode() {
        return code;
    }
    
}
