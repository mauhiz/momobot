package net.mauhiz.irc.bot.triggers.dispo;

/**
 * @author mauhiz
 */
public class Dispo {
    enum Present {
        LA, PAS_LA, SAIT_PAS;
    }
    private String channel;
    private long id;
    /**
     * La première heure (21h00).
     */
    private Present present1;
    /**
     * La deuxième heure (22h30).
     */
    private Present present2;
    /**
     * L'auth Qnet.
     */
    private String qauth;
    private String serverAlias;
    private java.sql.Date when;
    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    /**
     * @return the present1
     */
    public Present getPresent1() {
        return present1;
    }
    /**
     * @return the present2
     */
    public Present getPresent2() {
        return present2;
    }
    /**
     * @return the qauth
     */
    public String getQauth() {
        return qauth;
    }
    /**
     * @return the serverAlias
     */
    public String getServerAlias() {
        return serverAlias;
    }
    /**
     * @return the when
     */
    public java.sql.Date getWhen() {
        return when;
    }
    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(final String channel) {
        this.channel = channel;
    }
    /**
     * @param id
     *            the id to set
     */
    public void setId(final long id) {
        this.id = id;
    }
    /**
     * @param present1
     *            the present1 to set
     */
    public void setPresent1(final Present present1) {
        this.present1 = present1;
    }
    /**
     * @param present2
     *            the present2 to set
     */
    public void setPresent2(final Present present2) {
        this.present2 = present2;
    }
    /**
     * @param qauth
     *            the qauth to set
     */
    public void setQauth(final String qauth) {
        this.qauth = qauth;
    }
    /**
     * @param serverAlias
     *            the serverAlias to set
     */
    public void setServerAlias(final String serverAlias) {
        this.serverAlias = serverAlias;
    }
    /**
     * @param when
     *            the when to set
     */
    public void setWhen(final java.sql.Date when) {
        this.when = when;
    }
    
}
