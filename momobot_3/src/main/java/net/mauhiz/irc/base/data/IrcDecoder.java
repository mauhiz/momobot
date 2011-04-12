package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Ping;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.msg.ServerError;
import net.mauhiz.irc.base.msg.ServerMsg;
import net.mauhiz.irc.base.msg.SetTopic;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class IrcDecoder implements IrcPeer, IrcSpecialChars {

    static final Pattern CMD = Pattern.compile("([\\S^:]+) (.*)");
    static final Pattern FROM = Pattern.compile(":([\\S^:]+) (.*)");
    private static final Logger LOG = Logger.getLogger(IrcDecoder.class);
    static final Pattern TO = Pattern.compile("([\\S^:]+) (.*)");

    protected InetSocketAddress hostPort;

    protected IrcDecoder() {
        super();
    }

    /**
     * @param raw
     * @return raw IRC msg
     */
    public IIrcMessage buildFromRaw(String raw) {
        String work = raw;
        String fromStr = null;
        String toStr = null;
        Matcher m = FROM.matcher(work);
        if (m.matches()) {
            fromStr = m.group(1);
            work = m.group(2);
        }
        m = CMD.matcher(work);
        if (m.matches()) {
            String cmd = m.group(1);
            work = m.group(2);
            m = TO.matcher(work);
            String msg;
            if (m.matches()) {
                toStr = m.group(1);
                msg = m.group(2);
            } else {
                msg = work;
            }
            /* remove semicolon */
            if (msg.charAt(0) == ':') {
                msg = msg.substring(1);
            }
            Target from = decodeTarget(getServer(), fromStr);
            Target to = decodeTarget(getServer(), toStr);
            if (StringUtils.isNumeric(cmd)) {
                return newServerMsg(from, to, cmd, msg);
            }

            IrcCommands command = IrcCommands.valueOf(cmd);

            switch (command) {
                case NOTICE:
                    return new Notice(from, to, getServer(), msg);

                case PING:
                    return new Ping(from, to, getServer(), msg);

                case MODE:
                    return new Mode(from, to, getServer(), msg);

                case JOIN:
                    IrcChannel channel = getServer().findChannel(msg);
                    return new Join(from, getServer(), channel);

                case PART:
                    String partReason = StringUtils.substringAfter(msg, " :");
                    return new Part(getServer(), from, to, partReason);

                case PRIVMSG:
                    if (msg.charAt(0) == QUOTE_STX) {
                        msg = StringUtils.strip(msg, Character.toString(QUOTE_STX));
                        return CtcpFactory.decode(from, to, getServer(), msg);
                    }
                    return new Privmsg(from, to, getServer(), msg);

                case QUIT:
                    // FIXME the pattern for Quit is wrong, 'to' is actually the beginning of the message
                    return new Quit(from, to, getServer(), msg);

                case NICK:
                    return new Nick(getServer(), from, msg);

                case KICK:
                    String reason = StringUtils.substringAfter(msg, " :");
                    String nick = StringUtils.substringBefore(msg, " :");
                    IrcUser target = getServer().findUser(nick, false);
                    return new Kick(getServer(), from, (IrcChannel) to, target, reason);

                case ERROR:
                    return new ServerError(getServer(), cmd);

                case TOPIC:
                    return new SetTopic(from, to, getServer(), msg);

                default:
                    break;
            }
        }
        // TODO ERROR :Closing Link: by underworld2.no.quakenet.org (Registration Timeout)
        LOG.warn("Unknown message on server " + getServer() + ": " + raw);
        return null;
    }

    protected Target decodeTarget(IrcServer server, String fromStr) {
        if (fromStr == null) {
            return null;
        }
        if (MomoStringUtils.isChannelName(fromStr)) {
            return server.findChannel(fromStr);
        }

        HostMask mask = HostMask.getInstance(fromStr);

        if (mask == null) { // not a host mask
            if (StringUtils.contains(fromStr, '.')) {
                server.setIrcForm(fromStr);
                return server;
            }

            return server.findUser(fromStr, true);
        }

        return server.findUser(mask, true);
    }

    /**
     * @see net.mauhiz.irc.base.data.IrcPeer#getAddress()
     */
    public InetSocketAddress getAddress() {
        return hostPort;
    }

    protected abstract IrcServer getServer();

    /**
     * this method can be subclassed
     */
    protected IIrcMessage newServerMsg(Target from, Target to, String cmd, String msg) {
        return new ServerMsg(from, to, getServer(), cmd, msg);
    }
}
