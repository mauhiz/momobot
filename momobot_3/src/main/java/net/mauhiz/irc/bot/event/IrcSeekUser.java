package net.mauhiz.irc.bot.event;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.irc.base.data.IrcUser;

public class IrcSeekUser extends IrcUser {
    
    private List<String> history = new ArrayList<String>();
    private int ID;
    
    public IrcSeekUser(final IrcUser user) {
        super(user.getNick());
        ID = 1;
    }
    
    public IrcSeekUser(final String nick1, final int ID1) {
        super(nick1);
        ID = ID1;
    }
    
    public void addStringToHistory(final String str) {
        history.add(str);
    }
    
    public List<String> getHistory() {
        return history;
    }
    
    public int getID() {
        return ID;
    }
    
    public void setID(final int ID1) {
        ID = ID1;
    }
}
