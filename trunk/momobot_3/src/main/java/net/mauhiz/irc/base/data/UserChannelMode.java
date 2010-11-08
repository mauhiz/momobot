package net.mauhiz.irc.base.data;

public enum UserChannelMode {
    HALFOP('h', '%'), OP('o', '@'), VOICE('v', '+');
    
    public static UserChannelMode fromCmd(char cmd) {
        for (UserChannelMode ucm : values()) {
            if (ucm.getCmd() == cmd) {
                return ucm;
            }
        }
        
        throw new IllegalArgumentException("User Channel Mode not found : " + cmd);
    }
    private final char cmdFlag;
    
    private final char displayFlag;
    
    private UserChannelMode(char cmdFlag, char displayFlag) {
        this.cmdFlag = cmdFlag;
        this.displayFlag = displayFlag;
    }
    
    public char getCmd() {
        return cmdFlag;
    }
    
    public char getDisplay() {
        return displayFlag;
    }
}
