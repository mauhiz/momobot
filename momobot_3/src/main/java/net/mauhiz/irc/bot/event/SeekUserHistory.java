package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author Topper
 */
public class SeekUserHistory extends ArrayList<String> {
    private static final long serialVersionUID = 1;
    private int id;
    private final IrcUser user;
    
    /**
     * @param user1
     */
    public SeekUserHistory(IrcUser user1) {
        this(user1, 1);
    }
    
    /**
     * @param user1
     * @param id1
     */
    public SeekUserHistory(IrcUser user1, int id1) {
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
    public void setId(int id1) {
        id = id1;
    }
}
