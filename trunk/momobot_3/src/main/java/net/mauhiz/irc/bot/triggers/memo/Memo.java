package net.mauhiz.irc.bot.triggers.memo;

/**
 * @author mauhiz
 */
public class Memo {
    String channel;
    long id;
    String key;
    String serverAlias;
    String value;
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
     * @return the key
     */
    public String getKey() {
        return key;
    }
    /**
     * @return the serverAlias
     */
    public String getServerAlias() {
        return serverAlias;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @param serverAlias
     *            the serverAlias to set
     */
    public void setServerAlias(String serverAlias) {
        this.serverAlias = serverAlias;
    }
    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
