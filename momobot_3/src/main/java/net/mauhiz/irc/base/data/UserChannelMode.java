package net.mauhiz.irc.base.data;

public enum UserChannelMode {
    HALFOP('h', '%'), OP('o', '@'), VOICE('v', '+');

    public static UserChannelMode fromCmd(int utfChar) {
        for (UserChannelMode ucm : values()) {
            if (ucm.getCmd() == utfChar) {
                return ucm;
            }
        }

        return null;
    }

    public static UserChannelMode fromDisplay(int utfChar) {
        for (UserChannelMode ucm : values()) {
            if (ucm.getDisplay() == utfChar) {
                return ucm;
            }
        }

        return null;
    }

    public static boolean isDisplay(int utfChar) {
        return fromDisplay(utfChar) != null;
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
