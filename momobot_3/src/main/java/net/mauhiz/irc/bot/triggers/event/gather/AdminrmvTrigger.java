package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.IChannelEvent;
import net.mauhiz.irc.bot.event.Pickup;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class AdminrmvTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AdminrmvTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        IrcChannel chan = (IrcChannel) im.getTo();
        IChannelEvent event = chan.getEvt();
        if (event == null) {
            Privmsg msg = Privmsg.buildAnswer(im, "Aucun gather ou pickup n'est lance.");
            control.sendMsg(msg);
            return;
        }
        String[] whos = StringUtils.split(getArgs(im.getMessage()), ' ');
        if (ArrayUtils.isEmpty(whos)) {
            return;
        }

        if (event instanceof Gather) {
            for (String who : whos) {
                IrcUser target = im.getServerPeer().getNetwork().findUser(who, false);
                if (target == null) {
                    Privmsg msg = Privmsg.buildAnswer(im, who + " n'est pas sur " + chan.fullName());
                    control.sendMsg(msg);
                    continue;
                }
                String resp = AdminaddTrigger.replaceTu(((Gather) event).remove(target), target.getNick());
                Privmsg msg = Privmsg.buildAnswer(im, resp);
                control.sendMsg(msg);
            }

        } else if (event instanceof Pickup) {
            for (String who : whos) {
                IrcUser target = im.getServerPeer().getNetwork().findUser(who, false);
                if (target == null) {
                    Privmsg msg = Privmsg.buildAnswer(im, who + " n'est pas sur " + chan.fullName());
                    control.sendMsg(msg);
                    continue;
                }
                String resp = AdminaddTrigger.replaceTu(((Pickup) event).remove(target), target.getNick());
                Privmsg msg = Privmsg.buildAnswer(im, resp);
                control.sendMsg(msg);
            }

        }
    }
}
