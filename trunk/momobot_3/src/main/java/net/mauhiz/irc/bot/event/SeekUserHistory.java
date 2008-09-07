package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author Topper
 */
public class SeekUserHistory extends ArrayList<String> {
    private int id;
    private IrcUser user;
    
    /**
     * @param user1
     */
    public SeekUserHistory(final IrcUser user1) {
        this(user1, 1);
    }
    
    /**
     * @param user1
     * @param id1
     */
    public SeekUserHistory(final IrcUser user1, final int id1) {
        user = user1;
        id = id1;
    }
    
    /**
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return user.nick
     */
    public String getNick() {
        return user.getNick();
    }
    
    /**
     * @param id1
     */
    public void setId(final int id1) {
        id = id1;
    }
}
