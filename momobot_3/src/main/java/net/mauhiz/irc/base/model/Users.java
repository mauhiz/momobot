package net.mauhiz.irc.base.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author mauhiz
 */
public final class Users extends HashSet<IrcUser> {
    /**
     * serial.
     */
    private static final long serialVersionUID = 1L;
    /**
     * a chaque server sa liste d users.
     */
    private static final Map<IrcServer, Users> SERVERS = new HashMap<IrcServer, Users>();
    
    /**
     * @param server
     * @return users
     */
    public static Users getInstance(final IrcServer server) {
        Users servUsers = SERVERS.get(server);
        if (servUsers == null) {
            servUsers = new Users();
            SERVERS.put(server, servUsers);
        }
        return servUsers;
    }
    
    /**
     * private ctor
     */
    private Users() {
        super();
    }
    
    /**
     * @param mask
     * @param addIfNotFound
     *            means that all info from mask are safe, not just a query
     * @return IrcUser
     */
    public IrcUser findUser(final Mask mask, final boolean addIfNotFound) {
        if (mask == null) {
            throw new NullArgumentException("mask");
        }
        String nick = mask.getNick();
        if (nick == null) {
            throw new NullArgumentException("nick");
        }
        for (IrcUser user : this) {
            if (nick.equals(user.getNick())) {
                if (addIfNotFound && (user.getHost() == null || user.getUser() == null)) {
                    user.updateWithMask(mask);
                }
                return user;
            }
        }
        if (addIfNotFound) {
            IrcUser newUser = new IrcUser(mask.getNick());
            newUser.updateWithMask(mask);
            add(newUser);
            return newUser;
        }
        return null;
    }
    
    /**
     * @param nick
     * @param addIfNotFound
     * @return user
     */
    public IrcUser findUser(final String nick, final boolean addIfNotFound) {
        if (nick == null) {
            throw new NullArgumentException("nick");
        }
        for (IrcUser user : this) {
            if (nick.equals(user.getNick())) {
                return user;
            }
        }
        if (addIfNotFound) {
            IrcUser newUser = new IrcUser(nick);
            add(newUser);
            return newUser;
        }
        return null;
    }
    
    /**
     * @param smith
     * @return whether user is known
     */
    public boolean isKnown(final IrcUser smith) {
        if (contains(smith)) {
            return true;
        }
        return false;
    }
    
    /**
     * @param oldUser
     * @param newNick
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
