package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class UsersTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {

    /**
     * @param trigger
     */
    public UsersTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String args = getArgs(im.getMessage());
        String[] chanNames = StringUtils.split(args);
        if (ArrayUtils.isEmpty(chanNames)) {
            chanNames = new String[] { ((IrcChannel) im.getTo()).fullName() };
        }

        for (String chanName : chanNames) {
            IrcChannel channel = im.getServer().findChannel(chanName, false);
            if (channel == null) { // TODO /NAMES
                String chanMsg = "I am not on " + chanName;
                control.sendMsg(Privmsg.buildAnswer(im, chanMsg));

            } else {
                String chanMsg = channel.size() + " users on " + channel.fullName() + " : "
                        + StringUtils.join(channel.iterator(), " ");
                control.sendMsg(Privmsg.buildAnswer(im, chanMsg));
            }
        }
    }
}
