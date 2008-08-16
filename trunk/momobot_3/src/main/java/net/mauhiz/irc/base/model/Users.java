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
    static Map<IrcServer, Users> serverMap = new HashMap<IrcServer, Users>();
    
    /**
     * @param server
     * @return users
     */
    public static Users getInstance(final IrcServer server) {
        Users servUsers = serverMap.get(server);
        if (servUsers == null) {
            servUsers = new Users();
            serverMap.put(server, servUsers);
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
     * @return IrcUser
     */
    public IrcUser findUser(final Mask mask, final boolean addIfNotFound) {
        if (mask == null) {
            throw new NullArgumentException("mask");
        }
        for (IrcUser user : this) {
            if (mask.equals(user.getHostmask())) {
                return user;
            }
        }
        if (addIfNotFound) {
            IrcUser newUser = new IrcUser(mask);
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
}
