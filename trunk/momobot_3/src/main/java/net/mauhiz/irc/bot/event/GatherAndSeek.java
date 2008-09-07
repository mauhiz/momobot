package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.IrcChannel;

/**
 * @author Topper
 */
public class GatherAndSeek extends Gather {
    
    /**
     * @param channel1
     * @param nbPlayers
     */
    public GatherAndSeek(final IrcChannel channel1, final int nbPlayers) {
        super(channel1, nbPlayers);
        
    }
    
}
