package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
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
    public PickupTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        IrcChannel chan = im.getServer().findChannel(im.getTo());
        ChannelEvent evt = chan.getEvt();
        String resp;
        if (evt == null) {
            IrcUser user = im.getServer().findUser(new Mask(im.getFrom()), true);
            new Pickup(chan).add(user, null);
            
            resp = "Pickup lance par " + user.getNick();
        } else {
            resp = "Un " + evt.getClass().getSimpleName() + " est deja lance sur " + chan;
        }
        Privmsg msg = Privmsg.buildAnswer(im, resp);
        control.sendMsg(msg);
    }
}
