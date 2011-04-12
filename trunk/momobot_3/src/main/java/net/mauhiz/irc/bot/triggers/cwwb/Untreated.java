package net.mauhiz.irc.bot.triggers.cwwb;

import java.util.Calendar;

import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class Untreated {
    private long id;
    private String message;
    private IrcUser user;
    private Calendar when = Calendar.getInstance();

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the user
     */
    public IrcUser getUser() {
        return user;
    }

    /**
     * @return the when
     */
    public Calendar getWhen() {
        return when;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(IrcUser user) {
        this.user = user;
    }

    /**
     * @param when
     *            the when to set
     */
    public void setWhen(Calendar when) {
        this.when = when;
    }
}
