package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.AbstractIrcChannel;

/**
 * @author mauhiz
 */
public class QnetChannel extends AbstractIrcChannel {
    
    /**
     * @param chanName
     */
    public QnetChannel(final String chanName) {
        super(chanName);
        props = new QnetChannelProperties();
    }
    
}
