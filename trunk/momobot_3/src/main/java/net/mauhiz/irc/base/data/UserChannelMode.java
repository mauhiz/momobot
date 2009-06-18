package net.mauhiz.irc.base.data;

public enum UserChannelMode {
    HALFOP('h', '%'), OP('o', '@'), VOICE('v', '+');
    
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
