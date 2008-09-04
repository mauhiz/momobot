package net.mauhiz.irc.bot.triggers.cwwb;
import java.util.Calendar;

/**
 * @author mauhiz
 */
public class Untreated {
    private long id;
    private String message;
    private String user;
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
    public String getUser() {
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
    public void setId(final long id) {
        this.id = id;
    }
    
    /**
     * @param message
     *            the message to set
     */
    public void setMessage(final String message) {
        this.message = message;
    }
    
    /**
     * @param user
     *            the user to set
     */
    public void setUser(final String user) {
        this.user = user;
    }
    
    /**
     * @param when
     *            the when to set
     */
    public void setWhen(final Calendar when) {
        this.when = when;
    }
}
