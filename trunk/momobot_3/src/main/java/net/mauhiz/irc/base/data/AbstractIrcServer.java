package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.mauhiz.irc.MomoStringUtils;

import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcServer extends IrcDecoder implements IrcServer {
    
    protected static final Logger LOG = Logger.getLogger(AbstractIrcServer.class);
    private String alias;
    /**
     * a chaque server sa liste de channels.
     */
    private final Set<IrcChannel> channels = new ConcurrentSkipListSet<IrcChannel>();
    
    private IrcUser myself;
    /**
     * a chaque server sa liste d users.
     */
    private final Set<IrcUser> users = new ConcurrentSkipListSet<IrcUser>();
    /**
     * @param uriStr
     */
    protected AbstractIrcServer(String uriStr) {
        super();
        URI uri = URI.create(uriStr);
        hostPort = new InetSocketAddress(uri.getHost(), uri.getPort());
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
    public IrcChannel findChannel(String chanName) {
        return findChannel(chanName, true);
    }
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#findChannel(java.lang.String, boolean)
     */
    public IrcChannel findChannel(String chanName, boolean addIfNotFound) {
        //
        for (IrcChannel chan : channels) {
            if (chan.fullName().equalsIgnoreCase(chanName)) {
                return chan;
            }
        }
        if (addIfNotFound && MomoStringUtils.isChannelName(chanName)) {
            String chanLowerCase = chanName.toLowerCase(Locale.FRANCE);
            IrcChannel chan = newChannel(chanLowerCase);
            channels.add(chan);
            return chan;
        }
        
        return null;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#findUser(net.mauhiz.irc.base.data.Mask, boolean)
     */
    public IrcUser findUser(Mask mask, boolean addIfNotFound) {
        if (mask == null) {
            throw new NullArgumentException("mask");
        }
        String nick = mask.getNick();
        if (nick == null) {
            throw new IllegalArgumentException("Invalid mask (no nick): " + mask);
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
    public IrcUser findUser(String nick, boolean addIfNotFound) {
        if (nick == null) {
            throw new NullArgumentException("nick");
        } else if (nick.contains("!") || nick.contains(" ")) { // TODO better regexp
            throw new IllegalArgumentException("invalid nick : " + nick);
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
    public Set<IrcChannel> getChannelsForUser(IrcUser smith) {
        Set<IrcChannel> chans = new HashSet<IrcChannel>();
        for (IrcChannel chan : channels) {
            if (chan.contains(smith)) {
                chans.add(chan);
            }
        }
        return chans;
    }
    
    /**
     * @return the myself
     */
    public IrcUser getMyself() {
        return myself;
    }
    
    @Override
    protected IrcServer getServer() {
        return this;
    }
    
    /**
     * @return users
     */
    public Iterable<IrcUser> getUsers() {
        return users;
    }
    
    /**
     * @param channel
     */
    public void remove(IrcChannel channel) {
        channels.remove(channel);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#remove(net.mauhiz.irc.base.data.IrcUser)
     */
    public void remove(IrcUser quitter) {
        users.remove(quitter);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#setAlias(java.lang.String)
     */
    public void setAlias(String alias1) {
        alias = alias1;
    }
    
    /**
     * @param myself
     *            the myself to set
     */
    public void setMyself(IrcUser myself) {
        this.myself = myself;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getAlias();
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcServer#updateNick(net.mauhiz.irc.base.data.IrcUser, java.lang.String)
     */
    public void updateNick(IrcUser oldUser, String newNick) {
        if (oldUser == null) {
            /* we did not know him anyways. how so? */
            findUser(newNick, true);
        } else {
            oldUser.setNick(newNick);
        }
    }
}
