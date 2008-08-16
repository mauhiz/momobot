package net.mauhiz.irc.bot.triggers.fun;

import java.util.Iterator;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.math.RandomUtils;

/**
 * @author mauhiz
 */
public class RouletteTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public RouletteTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        String from = im.getTo();
        Channel wannabe = Channels.getInstance(im.getServer()).get(from);
        if (wannabe == null) {
            /* look args to determine channels */
            String args = getArgs(im.getMessage());
            wannabe = Channels.getInstance(im.getServer()).get(args);
            if (wannabe == null) {
                return;
            }
        }
        int index = RandomUtils.nextInt(wannabe.size());
        Iterator<IrcUser> uIter = wannabe.iterator();
        for (int i = 0; i < index; ++i) {
            uIter.next();
        }
        IrcUser random = uIter.next();
        Kick kick = new Kick(im.getServer(), null, null, wannabe.toString(), random.getNick(),
                "I, I know, how I feel when I'm around you");
        control.sendMsg(kick);
    }
}
