package net.mauhiz.irc.base.data;

import java.util.HashSet;

import net.mauhiz.irc.bot.event.ChannelEvent;

/**
 * @author mauhiz
 */
public class Channel extends HashSet<IrcUser> {
    /**
     * serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * event en cours (null si aucun)
     */
    private ChannelEvent evt;
    /**
     * nom du chan sans le prefixe.
     */
    private final String nom;
    /**
     * prefixe du chan (#, &amp;)
     */
    private final Character prefix;
    /**
     * flags du chan
     */
    private final ChannelProperties props = new ChannelProperties();
    
    /**
     * @param chanName
     */
    public Channel(final String chanName) {
        prefix = Character.valueOf(chanName.charAt(0));
        nom = chanName.substring(1);
    }
    
    /**
     * @see java.util.AbstractSet#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Channel)) {
            return false;
        }
        return toString().equals(o.toString());
    }
    
    /**
     * @return {@link #evt}
     */
    public ChannelEvent getEvt() {
        return evt;
    }
    
    /**
     * @return {@link #props}
     */
    public ChannelProperties getProps() {
        return props;
    }
    
    /**
     * @see java.util.AbstractSet#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    /**
     * @param channelEvent
     */
    public void setEvent(final ChannelEvent channelEvent) {
        evt = channelEvent;
    }
    
    /**
     * @return msg
     */
    public String stopEvent() {
        ChannelEvent localEvent = evt;
        setEvent(null);
        return localEvent.stop();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return prefix + nom;
    }
}
