package net.mauhiz.irc.bot.triggers.spam;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class SpamTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public SpamTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        /* TODO cross server */
        String msg = getArgs(im.getMessage());
        String targetNick = StringUtils.substringBefore(msg, " ");
        msg = StringUtils.substringAfter(msg, " ");
        IrcUser target = im.getServerPeer().getNetwork().findUser(targetNick, false);
        Privmsg spamMsg = new Privmsg(null, target, im.getServerPeer(), msg);
        long delay = 150;
        SpamRunnable spam = new SpamRunnable(spamMsg, control, delay);
        spam.startAs("Spam");
    }
}
