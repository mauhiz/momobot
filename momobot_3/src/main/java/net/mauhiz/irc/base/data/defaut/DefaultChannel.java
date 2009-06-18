package net.mauhiz.irc.base.data.defaut;

import net.mauhiz.irc.base.data.AbstractIrcChannel;
import net.mauhiz.irc.base.data.ChannelProperties;

/**
 * @author mauhiz
 * 
 */
class DefaultChannel extends AbstractIrcChannel {
    
    /**
     * @param chanName
     */
    public DefaultChannel(String chanName) {
        super(chanName);
        props = new ChannelProperties();
    }
    
}
