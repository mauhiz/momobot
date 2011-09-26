package net.mauhiz.irc.bot.event.seek;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.bot.event.Gather;

/**
 * @author Topper
 */
public class GatherAndSeek extends Gather {

    public GatherAndSeek(IrcChannel channel, int nbPlayers) {
        super(channel, nbPlayers);
    }
}
