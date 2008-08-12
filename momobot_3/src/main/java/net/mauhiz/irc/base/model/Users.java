package net.mauhiz.irc.base.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author mauhiz
 */
public class Users {
    static Map<IrcServer, Users> serverMap = new HashMap<IrcServer, Users>();
    
    /**
     * @param server
     * @return users
     */
    public static Users get(final IrcServer server) {
        Users servUsers = serverMap.get(server);
        if (servUsers == null) {
            servUsers = new Users();
            serverMap.put(server, servUsers);
        }
        return servUsers;
    }
    
    Map<Channel, Set<IrcUser>> userMap = new HashMap<Channel, Set<IrcUser>>();
    
    /**
     * private ctor
     */
    private Users() {
        super();
    }
    /**
     * @return user count
     */
    public int countUsers() {
        Set<IrcUser> allUsers = new HashSet<IrcUser>();
        for (Set<IrcUser> users : userMap.values()) {
            allUsers.addAll(users);
        }
        return allUsers.size();
    }
    
    /**
     * @param nick
     * @return user
     */
    public IrcUser findUser(final String nick) {
        if (nick == null) {
            throw new NullArgumentException("nick");
        }
        for (Set<IrcUser> users : userMap.values()) {
            for (IrcUser user : users) {
                if (nick.equals(user.getNick())) {
                    return user;
                }
            }
        }
        return null;
    }
    
    /**
     * @param smith
     * @return
     */
    public Set<Channel> getChannels(final IrcUser smith) {
        Set<Channel> channels = new HashSet<Channel>();
        for (Map.Entry<Channel, Set<IrcUser>> entry : userMap.entrySet()) {
            if (entry.getValue().contains(smith)) {
                channels.add(entry.getKey());
            }
        }
        return channels;
    }
    
    /**
     * @param channel
     * @return
     */
    public Set<IrcUser> getUsers(final Channel channel) {
        Set<IrcUser> chanUsers = userMap.get(channel);
        if (chanUsers == null) {
            chanUsers = new HashSet<IrcUser>();
            userMap.put(channel, chanUsers);
        }
        return chanUsers;
    }
    
    /**
     * @param smith
     * @return
     */
    public boolean isKnown(final IrcUser smith) {
        for (Set<IrcUser> users : userMap.values()) {
            if (users.contains(smith)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param channel
     * @param toPut
     * @return
     */
    public boolean put(final Channel channel, final IrcUser toPut) {
        Set<IrcUser> chanUsers = getUsers(channel);
        return chanUsers.add(toPut);
    }
}
