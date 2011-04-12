package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.Pickup;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class AdminaddTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param input
     * @param with
     * @return replacement
     */
    public static String replaceTu(String input, String with) {
        return StringUtils.replaceEach(input, new String[] { "tu es ", "Tu es " }, new String[] { with + " est ",
                with + " est " });
    }

    /**
     * @param trigger
     *            le trigger
     */
    public AdminaddTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        IrcChannel chan = (IrcChannel) im.getTo();
        ChannelEvent event = chan.getEvt();
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
                IrcUser target = im.getServer().findUser(who, false);
                if (target == null) {
                    Privmsg msg = Privmsg.buildAnswer(im, who + " n'est pas sur " + chan);
                    control.sendMsg(msg);
                    continue;
                }
                String resp = replaceTu(((Gather) event).add(target), target.getNick());
                Privmsg msg = Privmsg.buildAnswer(im, resp);
                control.sendMsg(msg);
            }

        } else if (event instanceof Pickup) {
            String team = whos[whos.length - 1];
            Pickup pick = (Pickup) event;
            int max = whos.length;
            if (pick.assignTeam(team) != null) {
                --max;
            }
            for (int i = 0; i < max; i++) {
                IrcUser target = im.getServer().findUser(whos[i], false);
                if (target == null) {
                    Privmsg msg = Privmsg.buildAnswer(im, whos[i] + " n'est pas sur " + chan);
                    control.sendMsg(msg);
                    continue;
                }
                String resp = replaceTu(pick.add(target, team), target.getNick());
                Privmsg msg = Privmsg.buildAnswer(im, resp);
                control.sendMsg(msg);
            }

        }
    }
}
