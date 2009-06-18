package net.mauhiz.irc.bot.triggers.cwwb;
/**
 * @author abby
 */
public enum ServerStatus {
    OFF(2), ON(1), UNKOWN(0);
    
    /** L'attribut qui contient la valeur associe a l'enum */
    private int code;
    
    /**
     * Le constructeur qui associe une valeur a l'enum
     * 
     * @param code
     */
    private ServerStatus(int code) {
        this.code = code;
    }
    
    /** @return la valeur de l'enum */
    public int getCode() {
        return code;
    }
    
}
