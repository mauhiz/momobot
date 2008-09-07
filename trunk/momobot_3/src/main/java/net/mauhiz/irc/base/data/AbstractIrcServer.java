package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.irc.MomoStringUtils;
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

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcServer extends AbstractHookable<IrcServer> implements IrcCommands, IrcServer {
    
    static final Pattern CMD = Pattern.compile("([\\S^:]+) (.*)");
    static final Pattern FROM = Pattern.compile(":([\\S^:]+) (.*)");
    private static final Logger LOG = Logger.getLogger(AbstractIrcServer.class);
    static final Pattern TO = Pattern.compile("([\\S^:]+) (.*)");
    private String alias;
    /**
     * a chaque server sa liste de channels.
     */
    private final Set<IrcChannel> channels = new HashSet<IrcChannel>();
    
    private final InetSocketAddress hostPort;
    private String myFullName;
    private String myLogin;
    private String myNick;
    /**
     * a chaque server sa liste d users.
     */
    private final Set<IrcUser> users = new HashSet<IrcUser>();
    /**
     * @param uriStr
     */
    protected AbstractIrcServer(final String uriStr) {
        super();
        URI uri = URI.create(uriStr);
        hostPort = new InetSocketAddress(uri.getHost(), uri.getPort());
    }
    /**
     * @param raw
     * @return raw IRC msg
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
                msg = StringUtils.substringBefore(msg, " :");
                return new Part(this, from, to, msg, reason);
            } else if (PRIVMSG.equals(cmd)) {
                return new Privmsg(from, to, this, msg);
            } else if (QUIT.equals(cmd)) {
                return new Quit(from, to, this, msg);
            } else if (NICK.equals(cmd)) {
                return new Nick(this, from, msg);
            } else if (KICK.equals(cmd)) {
                String reason = StringUtils.substringAfter(msg, " :");
                msg = StringUtils.substringBefore(msg, " :");
                return new Kick(this, from, null, to, msg, reason);
            } else if (ERROR.equals(cmd)) {
                return new ServerError(this, cmd);
            }
        }
        // TODO ERROR :Closing Link: by underworld2.no.quakenet.org (Registration Timeout)
        LOG.warn("Unknown message on server " + getAlias() + ": " + raw);
        return null;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#countUsers()
     */
    public int countUsers() {
        return users.size();
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#findChannel(java.lang.String)
     */
    public IrcChannel findChannel(final String chanName) {
        return findChannel(chanName, true);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#findChannel(java.lang.String, boolean)
     */
    public IrcChannel findChannel(final String chanName, final boolean addIfNotFound) {
        // 
        for (IrcChannel chan : channels) {
            if (chan.toString().equalsIgnoreCase(chanName)) {
                return chan;
            }
        }
        if (addIfNotFound && MomoStringUtils.isChannelName(chanName)) {
            final String chanLowerCase = chanName.toLowerCase(Locale.FRANCE);
            IrcChannel chan = newChannel(chanLowerCase);
            channels.add(chan);
            return chan;
        }
        return null;
        
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#findUser(net.mauhiz.irc.base.data.Mask, boolean)
     */
    public IrcUser findUser(final Mask mask, final boolean addIfNotFound) {
        if (mask == null) {
            throw new NullArgumentException("mask");
        }
        String nick = mask.getNick();
        if (nick == null) {
            throw new NullArgumentException("nick");
        }
        for (IrcUser user : users) {
            if (nick.equalsIgnoreCase(user.getNick())) {
                if (addIfNotFound && (user.getHost() == null || user.getUser() == null)) {
                    user.updateWithMask(mask);
                }
                return user;
            }
        }
        if (addIfNotFound) {
            IrcUser newUser = newUser(mask.getNick());
            newUser.updateWithMask(mask);
            users.add(newUser);
            return newUser;
        }
        return null;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#findUser(java.lang.String, boolean)
     */
    public IrcUser findUser(final String nick, final boolean addIfNotFound) {
        if (nick == null) {
            throw new NullArgumentException("nick");
        }
        for (IrcUser user : users) {
            if (nick.equalsIgnoreCase(user.getNick())) {
                return user;
            }
        }
        if (addIfNotFound) {
            IrcUser newUser = newUser(nick);
            users.add(newUser);
            return newUser;
        }
        return null;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getAddress()
     */
    public InetSocketAddress getAddress() {
        return hostPort;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getAlias()
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getChannels()
     */
    public Iterable<IrcChannel> getChannels() {
        return channels;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getChannelsForUser(net.mauhiz.irc.base.data.IrcUser)
     */
    public Set<IrcChannel> getChannelsForUser(final IrcUser smith) {
        Set<IrcChannel> chans = new HashSet<IrcChannel>();
        for (IrcChannel chan : channels) {
            if (chan.contains(smith)) {
                chans.add(chan);
            }
        }
        return chans;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getMyFullName()
     */
    public String getMyFullName() {
        return myFullName;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getMyLogin()
     */
    public String getMyLogin() {
        return myLogin;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#getMyNick()
     */
    public String getMyNick() {
        return myNick;
    }
    
    /**
     * @return users
     */
    public Iterable<IrcUser> getUsers() {
        return users;
    }
    
    /**
     * @param chanLowerCase
     * @return Channel instance
     */
    protected abstract IrcChannel newChannel(final String chanLowerCase);
    
    /**
     * @param nick
     * @return IrcUser instance
     */
    protected abstract IrcUser newUser(final String nick);
    
    /**
     * @param channel
     */
    public void remove(final IrcChannel channel) {
        channels.remove(channel);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#remove(net.mauhiz.irc.base.data.IrcUser)
     */
    public void remove(final IrcUser quitter) {
        users.remove(quitter);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#setAlias(java.lang.String)
     */
    public void setAlias(final String alias1) {
        alias = alias1;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#setMyFullName(java.lang.String)
     */
    public void setMyFullName(final String myFullName1) {
        myFullName = myFullName1;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#setMyLogin(java.lang.String)
     */
    public void setMyLogin(final String myLogin1) {
        myLogin = myLogin1;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#setMyNick(java.lang.String)
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
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#updateNick(net.mauhiz.irc.base.data.IrcUser, java.lang.String)
     */
    public void updateNick(final IrcUser oldUser, final String newNick) {
        if (oldUser == null) {
            /* we did not know him anyways. how so? */
            findUser(newNick, true);
        } else {
            oldUser.setNick(newNick);
        }
    }
}
