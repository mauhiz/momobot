package net.mauhiz.irc.base.data;

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
public class IrcDecoder implements IrcSpecialChars, IIrcDecoder {

    static final Pattern CMD = Pattern.compile("([\\S^:]+) (.*)");
    static final Pattern FROM = Pattern.compile(":([\\S^:]+) (.*)");
    public static final IIrcDecoder INSTANCE;
    private static final Logger LOG = Logger.getLogger(IrcDecoder.class);
    static final Pattern TO = Pattern.compile("([\\S^:]+) (.*)");

    static {
        INSTANCE = new IrcDecoder();
    }

    public static IIrcDecoder getInstance() {
        return INSTANCE;
    }

    private IrcDecoder() {
        super();
    }

    /**
     * @param raw
     * @return raw IRC msg
     */
    public IIrcMessage buildFromRaw(IIrcServerPeer server, String raw) {
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
            Target from = decodeTarget(server, fromStr);
            Target to = decodeTarget(server, toStr);
            if (StringUtils.isNumeric(cmd)) {
                return new ServerMsg(from, to, server, cmd, msg);
            }

            IrcCommands command = IrcCommands.valueOf(cmd);

            switch (command) {
                case NOTICE:
                    return new Notice(from, to, server, msg);

                case PING:
                    return new Ping(from, server, msg);

                case MODE:
                    return new Mode(from, to, server, msg);

                case JOIN:
                    IrcChannel channel = server.getNetwork().findChannel(msg);
                    return new Join(from, server, channel);

                case PART:
                    String partReason = StringUtils.substringAfter(msg, " :");
                    return new Part(server, (IrcUser) from, (IrcChannel) to, partReason);

                case PRIVMSG:
                    if (msg.charAt(0) == QUOTE_STX) {
                        msg = StringUtils.strip(msg, Character.toString(QUOTE_STX));
                        return CtcpFactory.decode(from, to, server, msg);
                    }
                    return new Privmsg(from, to, server, msg);

                case QUIT:
                    // FIXME the pattern for Quit is wrong, 'to' is actually the beginning of the message
                    return new Quit(from, server, toStr + " " + msg);

                case NICK:
                    return new Nick(server, from, msg);

                case KICK:
                    String reason = StringUtils.substringAfter(msg, " :");
                    String nick = StringUtils.substringBefore(msg, " :");
                    IrcUser target = server.getNetwork().findUser(nick, false);
                    return new Kick(server, from, (IrcChannel) to, target, reason);

                case ERROR:
                    // TODO ERROR :Closing Link: by underworld2.no.quakenet.org (Registration Timeout)
                    return new ServerError(server, cmd);

                case TOPIC:
                    return new SetTopic(from, to, server, msg);

                default:
                    break;
            }
        }
        LOG.warn("Unknown message on server " + server + ": " + raw);
        return null;
    }

    protected Target decodeTarget(IIrcServerPeer peer, String fromStr) {
        if (fromStr == null) {
            return null;
        }
        IrcNetwork server = peer.getNetwork();
        if (MomoStringUtils.isChannelName(fromStr)) {
            return server.findChannel(fromStr);
        }

        HostMask mask = HostMask.getInstance(fromStr);

        if (mask == null) { // not a host mask
            if (StringUtils.contains(fromStr, '.')) {
                peer.setIrcForm(fromStr);
                return peer;
            }

            return server.findUser(fromStr, true);
        }

        return server.findUser(mask, true);
    }
}
