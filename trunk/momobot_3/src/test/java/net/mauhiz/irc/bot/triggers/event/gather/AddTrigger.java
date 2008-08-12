package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.Pickup;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class AddTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AddTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        IrcServer server = im.getServer();
        Channel chan = Channels.get(server).getChannel(im.getTo());
        ChannelEvent event = ChannelEvent.CHANNEL_EVENTS.get(chan);
        if (event == null) {
            Privmsg msg = Privmsg.buildAnswer(im, "Aucun gather ou pickup n'est lance.");
            control.sendMsg(msg);
            return;
        }
        IrcUser user = Users.get(server).findUser(im.getFrom());
        if (event instanceof Gather) {
            Privmsg msg = Privmsg.buildAnswer(im, ((Gather) event).add(user).toString());
            control.sendMsg(msg);
        } else if (event instanceof Pickup) {
            Privmsg msg = Privmsg.buildAnswer(im, ((Pickup) event).add(user, getArgs(im.getMessage())));
            control.sendMsg(msg);
        }
    }
}
