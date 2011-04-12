package net.mauhiz.irc.bot.triggers.event;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

/**
 * @author mauhiz
 */
public class StopTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public StopTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        IrcChannel chan = (IrcChannel) im.getTo();
        ChannelEvent evt = chan.getEvt();
        if (evt != null) {
            Privmsg msg = Privmsg.buildAnswer(im, chan.stopEvent());
            control.sendMsg(msg);
        }
    }
}
