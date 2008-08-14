package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Ping;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.msg.ServerMsg;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcServer {
    static final Pattern CMD = Pattern.compile("([\\S^:]+) (.*)");
    static final Pattern FROM = Pattern.compile(":([\\S^:]+) (.*)");
    static final String JOIN = "JOIN";
    static final String KICK = "KICK";
    private static final Logger LOG = Logger.getLogger(IrcServer.class);
    static final String MODE = "MODE";
    static final Object NICK = "NICK";
    static final String NOTICE = "NOTICE";
    static final String PART = "PART";
    static final String PING = "PING";
    static final String PRIVMSG = "PRIVMSG";
    static final String QUIT = "QUIT";
    static final Pattern TO = Pattern.compile("([\\S^:]+) (.*)");
    private String alias;
    InetSocketAddress hostPort;
    private String myFullName;
    private String myLogin;
    private String myNick;
    
    /**
     * default ctor
     */
    public IrcServer() {
        super();
    }
    
    /**
     * @param uriStr
     * @throws URISyntaxException
     */
    public IrcServer(final String uriStr) throws URISyntaxException {
        this();
        buildFromURI(uriStr);
    }
    
    /**
     * @param raw
     * @return
     */
    public IIrcMessage buildFromRaw(final String raw) {
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
                return new ServerMsg(from, to, this, cmd, msg);
            } else if (NOTICE.equals(cmd)) {
                return new Notice(from, to, this, msg);
            } else if (PING.equals(cmd)) {
                return new Ping(from, to, this, msg);
            } else if (MODE.equals(cmd)) {
                return new Mode(from, to, this, msg);
            } else if (JOIN.equals(cmd)) {
                return new Join(from, this, msg);
            } else if (PART.equals(cmd)) {
                String reason = StringUtils.substringAfter(msg, " :");
                msg = StringUtils.substringAfter(msg, " :");
                return new Part(this, from, to, msg, reason);
            } else if (PRIVMSG.equals(cmd)) {
                return new Privmsg(from, to, this, msg);
            } else if (QUIT.equals(cmd)) {
                return new Quit(from, to, this, msg);
            } else if (NICK.equals(cmd)) {
                return new Nick(this, work);
            }
        }
        // TODO ERROR :Closing Link: by underworld2.no.quakenet.org (Registration Timeout)
        LOG.warn("Unknown message on server " + getAlias() + ": " + raw);
        return null;
    }
    
    /**
     * @param uriStr
     * @throws URISyntaxException
     */
    public final void buildFromURI(final String uriStr) throws URISyntaxException {
        URI uri = new URI(uriStr);
        hostPort = new InetSocketAddress(uri.getHost(), uri.getPort());
    }
    
    /**
     * @return le nombre d'users
     */
    public int countUsers() {
        return Users.get(this).size();
    }
    
    /**
     * @return {@link #hostPort}
     */
    public InetSocketAddress getAddress() {
        return hostPort;
    }
    
    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * @return the {@link #myFullName}
     */
    public String getMyFullName() {
        return myFullName;
    }
    
    /**
     * @return the myLogin
     */
    public String getMyLogin() {
        return myLogin;
    }
    
    /**
     * @return the myNick
     */
    public String getMyNick() {
        return myNick;
    }
    
    /**
     * @param alias1
     *            the alias to set
     */
    public void setAlias(final String alias1) {
        alias = alias1;
    }
    
    /**
     * @param myFullName1
     */
    public void setMyFullName(final String myFullName1) {
        myFullName = myFullName1;
    }
    
    /**
     * @param myLogin1
     *            the myLogin to set
     */
    public void setMyLogin(final String myLogin1) {
        myLogin = myLogin1;
    }
    
    /**
     * @param myNick1
     *            the myNick to set
     */
    public void setMyNick(final String myNick1) {
        myNick = myNick1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
