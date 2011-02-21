package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author Topper
 */

// TODO : renommer cette classe, ou la fusionner avec les classes utilisateur existantes....
public class SeekUserHistory extends ArrayList<String> {
    private static final long serialVersionUID = 1;
    private int id;
    private final IrcUser user;
    
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
        super();
        user = user1;
        id = id1;
    }
    
    /**
     * Renvoie un entier identifiant de mani�re unique un utilisateur
     * 
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
