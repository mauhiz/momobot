package net.mauhiz.irc.base.data;

import net.mauhiz.irc.bot.event.ChannelEvent;

/**
 * @author mauhiz
 */
public interface IrcChannel extends Iterable<IrcUser>, IHookable<IrcChannel> {
    
    void add(IrcUser truite);
    
    boolean contains(IrcUser smith);
    
    ChannelEvent getEvt();
    
    void remove(IrcUser toRemove);
    
    int size();
    
    String stopEvent();
}
