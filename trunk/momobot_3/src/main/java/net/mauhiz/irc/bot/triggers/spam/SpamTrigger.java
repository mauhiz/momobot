package net.mauhiz.irc.bot.triggers.spam;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

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
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        /* TODO cross server */
        String msg = getArgs(im.getMessage());
        String target = StringUtils.substringBefore(msg, " ");
        msg = StringUtils.substringAfter(msg, " ");
        Privmsg spamMsg = new Privmsg(null, target, im.getServer(), msg);
        long delay = 150;
        SpamRunnable spam = new SpamRunnable(spamMsg, control, delay);
        spam.startAs("Spam");
    }
}
