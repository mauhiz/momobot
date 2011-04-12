package net.mauhiz.irc.base.msg;

import java.util.Set;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.data.UserChannelMode;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Mode extends AbstractIrcMessage {
    private static final Logger LOG = Logger.getLogger(Mode.class);

    public static boolean isModifier(char c) {
        return c == '+' || c == '-';
    }

    private final String message;

    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public Mode(Target from, Target to, IrcServer server) {
        this(from, to, server, null);
    }

    /**
     * @param from
     * @param to
     * @param ircServer
     * @param message
     */
    public Mode(Target from, Target to, IrcServer ircServer, String message) {
        super(from, to, ircServer);
        this.message = message;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("MODE ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(message);
        return sb.toString();
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public void process(IIrcControl control) {
        String[] toks = StringUtils.split(message, ' ');

        if (isToChannel()) {
            IrcChannel chan = (IrcChannel) to;
            int argIdx = 0;
            String modeInfo = toks[argIdx++]; // consume 1st argument
            boolean set = false;

            for (int idx = 0; idx < modeInfo.length(); idx++) {
                char next = modeInfo.charAt(idx);

                if (isModifier(next)) {
                    set = next == '+';
                    continue;
                }

                char modeItem = modeInfo.charAt(idx);
                UserChannelMode newMode = UserChannelMode.fromCmd(modeItem);

                if (newMode == null) {
                    // channel mode
                    chan.getProperties().process(set, modeItem);

                } else {
                    // user mode
                    String target = toks[argIdx++];
                    IrcUser targetUser = server.findUser(target, true);
                    Set<UserChannelMode> userModes = chan.getModes(targetUser);

                    if (set) {
                        userModes.add(newMode);

                    } else {
                        userModes.remove(newMode);
                    }
                }
            }
        } else {
            IrcUser target = (IrcUser) to;
            char modifier = message.charAt(0);
            char mode = message.charAt(1);
            if (mode == 'i') {
                target.getProperties().setInvisible(modifier == '+');

            } else {
                LOG.warn("TODO process user mode: " + message);
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "* " + niceFromDisplay() + " sets mode: " + message + " " + to;
    }
}
