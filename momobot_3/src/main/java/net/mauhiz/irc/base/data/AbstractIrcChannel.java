package net.mauhiz.irc.base.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.mauhiz.irc.bot.event.ChannelEvent;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcChannel extends AbstractHookable<IrcChannel> implements IrcChannel {
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
    protected ChannelProperties props;
    /**
     * users sur le channel
     */
    protected final Set<IrcUser> users = new HashSet<IrcUser>();
    
    /**
     * @param chanName
     */
    protected AbstractIrcChannel(final String chanName) {
        prefix = Character.valueOf(chanName.charAt(0));
        nom = chanName.substring(1);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcChannel#add(net.mauhiz.irc.base.data.IrcUser)
     */
    @Override
    public void add(final IrcUser truite) {
        users.add(truite);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcChannel#contains(net.mauhiz.irc.base.data.IrcUser)
     */
    @Override
    public boolean contains(final IrcUser smith) {
        return users.contains(smith);
    }
    
    /**
     * @see java.util.AbstractSet#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof IrcChannel)) {
            return false;
        }
        return toString().equals(o.toString());
    }
    
    /**
     * @return running event
     */
    public ChannelEvent getEvt() {
        return getHookedObject(ChannelEvent.class);
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
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<IrcUser> iterator() {
        return users.iterator();
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcChannel#remove(net.mauhiz.irc.base.data.IrcUser)
     */
    @Override
    public void remove(final IrcUser toRemove) {
        users.remove(toRemove);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcChannel#size()
     */
    @Override
    public int size() {
        return users.size();
    }
    
    /**
     * @return msg
     */
    public String stopEvent() {
        ChannelEvent localEvent = getEvt();
        if (localEvent == null) {
            return "";
        }
        removeHook(localEvent.getClass());
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
