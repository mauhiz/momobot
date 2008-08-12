package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Pickup;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class PickupTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PickupTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        Channel chan = Channels.get(im.getServer()).getChannel(im.getTo());
        ChannelEvent evt = ChannelEvent.CHANNEL_EVENTS.get(chan);
        String resp;
        if (evt != null) {
            resp = "Un " + evt.getClass().getSimpleName() + " est déjà lancé sur " + chan;
        } else {
            IrcUser user = Users.get(im.getServer()).findUser(im.getFrom());
            new Pickup(chan);
            
            resp = "Pickup lancé par " + user;
        }
        Privmsg msg = Privmsg.buildAnswer(im, resp);
        control.sendMsg(msg);
    }
}
