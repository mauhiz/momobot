package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.AbstractIrcChannel;

/**
 * @author mauhiz
 */
class QnetChannel extends AbstractIrcChannel {
    
    /**
     * @param chanName
     */
    public QnetChannel(String chanName) {
        super(chanName);
        props = new QnetChannelProperties();
    }
}
