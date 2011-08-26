package net.mauhiz.irc.base.data;

import net.mauhiz.irc.base.msg.Action;
import net.mauhiz.irc.base.msg.Ctcp;
import net.mauhiz.irc.base.msg.CtcpVersion;
import net.mauhiz.irc.base.msg.DccChatRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public enum CtcpFactory {
    ;
    private static final Logger LOG = Logger.getLogger(CtcpFactory.class);

    public static Ctcp decode(IIrcServerPeer server, Target from, Target to, String msg) {
        final String command = StringUtils.substringBefore(msg, " ");
        String ctcpContent = StringUtils.substringAfter(msg, " ");

        if (Action.COMMAND.equals(command)) {
            return new Action(server, from, to, ctcpContent);

        } else if (CtcpVersion.COMMAND.equals(command)) {
            return new CtcpVersion(server, from, to, ctcpContent);

        } else if (DccChatRequest.COMMAND.equals(command)) {
            if (ctcpContent.startsWith("CHAT CHAT ")) {
                return new DccChatRequest(server, from, to, ctcpContent);
            }
        }

        LOG.warn("Unknown CTCP: " + command);
        return new Ctcp(server, from, to, ctcpContent) {

            @Override
            protected String getCommand() {
                return command;
            }
        };
    }
}
