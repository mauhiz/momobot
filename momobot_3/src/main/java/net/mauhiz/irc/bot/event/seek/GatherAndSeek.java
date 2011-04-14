package net.mauhiz.irc.bot.event.seek;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.bot.event.Gather;

/**
 * @author Topper
 */
public class GatherAndSeek extends Gather {
    
    /**
     * @param channel1
     * @param nbPlayers
     */
    public GatherAndSeek(IrcChannel channel1, int nbPlayers) {
        super(channel1, nbPlayers);
    }
}
