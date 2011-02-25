package net.mauhiz.irc.base.data;

import net.mauhiz.irc.base.msg.Action;
import net.mauhiz.irc.base.msg.Ctcp;
import net.mauhiz.irc.base.msg.DccChatRequest;

import org.apache.commons.lang.StringUtils;

public class CtcpFactory {

    public static Ctcp decode(String from, String to, IrcServer server, String msg) {
        String command = StringUtils.substringBefore(msg, " ");
        String ctcpContent = StringUtils.substringAfter(msg, " ");

        if (Action.COMMAND.equals(command)) {
            return new Action(from, to, server, ctcpContent);

        } else if (DccChatRequest.COMMAND.equals(command)) {
            if (ctcpContent.startsWith("CHAT CHAT ")) {
                return new DccChatRequest(from, to, server, ctcpContent);
            }
        }

        return null;
    }
}
