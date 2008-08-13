package net.mauhiz.irc.base.data;

import java.util.HashSet;

import net.mauhiz.irc.base.data.qnet.QnetChannelProperties;
import net.mauhiz.irc.bot.event.ChannelEvent;

/**
 * @author mauhiz
 */
public class Channel extends HashSet<IrcUser> {
    /**
     * serial
     */
    private static final long serialVersionUID = 1L;
    private ChannelEvent evt;
    String nom;
    
    Character prefix;
    
    ChannelProperties props = new QnetChannelProperties();
    
    /**
     * @param chanName
     */
    public Channel(final String chanName) {
        if (Character.isLetterOrDigit(chanName.charAt(0))) {
            nom = chanName;
        } else {
            prefix = Character.valueOf(chanName.charAt(0));
            nom = chanName.substring(1);
        }
    }
    
    public ChannelEvent getEvt() {
        return evt;
    }
    
    /**
     * @param channelEvent
     */
    public void setEvent(final ChannelEvent channelEvent) {
        evt = channelEvent;
    }
    
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
        if (prefix == null) {
            return nom;
        }
        return prefix + nom;
    }
}
