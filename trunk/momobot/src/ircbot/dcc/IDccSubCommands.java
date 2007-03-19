package ircbot.dcc;

import ircbot.ICtcpCommands;
import ircbot.IIrcSpecialChars;

/**
 * @author viper
 */
public interface IDccSubCommands extends ICtcpCommands, IIrcSpecialChars {
    /**
     * .
     */
    String DCC_ACCEPT = E_DCC + SPC + "ACCEPT";
    /**
     * .
     */
    String DCC_CHAT   = E_DCC + SPC + "CHAT";
    /**
     * .
     */
    String DCC_RESUME = E_DCC + SPC + "RESUME";
    /**
     * .
     */
    String DCC_SEND   = E_DCC + SPC + "SEND";
}
