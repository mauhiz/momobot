package net.mauhiz.irc.base.data;

import net.mauhiz.util.UtfChar;

public enum UserChannelMode {
    HALFOP('h', '%'), OP('o', '@'), VOICE('v', '+');

    public static UserChannelMode fromCmd(UtfChar utfChar) {
        for (UserChannelMode ucm : values()) {
            if (ucm.getCmd().equals(utfChar)) {
                return ucm;
            }
        }

        return null;
    }

    public static UserChannelMode fromDisplay(UtfChar utfChar) {
        for (UserChannelMode ucm : values()) {
            if (ucm.getDisplay().equals(utfChar)) {
                return ucm;
            }
        }

        return null;
    }

    public static boolean isDisplay(UtfChar utfChar) {
        return fromDisplay(utfChar) != null;
    }

    private final UtfChar cmdFlag;
    private final UtfChar displayFlag;

    private UserChannelMode(char cmdFlag, char displayFlag) {
        this(UtfChar.valueOf(cmdFlag), UtfChar.valueOf(displayFlag));
    }

    private UserChannelMode(UtfChar cmdFlag, UtfChar displayFlag) {
        this.cmdFlag = cmdFlag;
        this.displayFlag = displayFlag;
    }

    public UtfChar getCmd() {
        return cmdFlag;
    }

    public UtfChar getDisplay() {
        return displayFlag;
    }
}
