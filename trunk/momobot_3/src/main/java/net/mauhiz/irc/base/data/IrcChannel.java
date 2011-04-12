package net.mauhiz.irc.base.data;

import java.util.Set;

import net.mauhiz.irc.bot.event.ChannelEvent;

/**
 * @author mauhiz
 */
public interface IrcChannel extends Iterable<IrcUser>, Comparable<IrcChannel>, Target {

    void add(IrcUser smith);

    boolean contains(IrcUser smith);

    String fullName();

    ChannelEvent getEvt();

    Set<UserChannelMode> getModes(IrcUser smith);

    ChannelProperties getProperties();

    boolean isTopicEditable(IrcUser user);

    void remove(IrcUser toRemove);

    int size();

    String stopEvent();
}
