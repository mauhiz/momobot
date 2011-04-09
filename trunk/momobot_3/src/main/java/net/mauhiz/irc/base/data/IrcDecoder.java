package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public abstract class IrcDecoder implements IrcCommands, IrcPeer, IrcSpecialChars {

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
        String from = null;
        String to = null;
        Matcher m = FROM.matcher(work);
        if (m.matches()) {
            from = m.group(1);
            work = m.group(2);
        }
        m = CMD.matcher(work);
        if (m.matches()) {
            String cmd = m.group(1);
            work = m.group(2);
            m = TO.matcher(work);
            String msg;
            if (m.matches()) {
                to = m.group(1);
                msg = m.group(2);
            } else {
                msg = work;
            }
            /* remove semicolon */
            if (msg.charAt(0) == ':') {
                msg = msg.substring(1);
            }
            if (StringUtils.isNumeric(cmd)) {
                return newServerMsg(from, to, cmd, msg);
            } else if (NOTICE.equals(cmd)) {
                return new Notice(from, to, getServer(), msg);
            } else if (PING.equals(cmd)) {
                return new Ping(from, to, getServer(), msg);
            } else if (MODE.equals(cmd)) {
                return new Mode(from, to, getServer(), msg);
            } else if (JOIN.equals(cmd)) {
                return new Join(from, getServer(), msg);
            } else if (PART.equals(cmd)) {
                String reason = StringUtils.substringAfter(msg, " :");
                msg = StringUtils.substringBefore(msg, " :");
                return new Part(getServer(), from, to, msg, reason);
            } else if (PRIVMSG.equals(cmd)) {
                if (msg.charAt(0) == QUOTE_STX) {
                    msg = StringUtils.strip(msg, Character.toString(QUOTE_STX));
                    return CtcpFactory.decode(from, to, getServer(), msg);
                }
                return new Privmsg(from, to, getServer(), msg);
            } else if (QUIT.equals(cmd)) {
                return new Quit(from, to, getServer(), msg);
            } else if (NICK.equals(cmd)) {
                return new Nick(getServer(), from, msg);
            } else if (KICK.equals(cmd)) {
                String reason = StringUtils.substringAfter(msg, " :");
                msg = StringUtils.substringBefore(msg, " :");
                return new Kick(getServer(), from, null, to, msg, reason);
            } else if (ERROR.equals(cmd)) {
                return new ServerError(getServer(), cmd);
            } else if (TOPIC.equals(cmd)) {
                return new SetTopic(from, to, getServer(), msg);
            }
        }
        // TODO ERROR :Closing Link: by underworld2.no.quakenet.org (Registration Timeout)
        LOG.warn("Unknown message on server " + getServer().getAlias() + ": " + raw);
        return null;
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
    protected IIrcMessage newServerMsg(String from, String to, String cmd, String msg) {
        return new ServerMsg(from, to, getServer(), cmd, msg);
    }
}
