package net.mauhiz.irc.bot.triggers.spam;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class SpamTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public SpamTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        /* FIXME use args */
        Privmsg spamMsg = new Privmsg(null, null, null, null);
        long delay = 150;
        AbstractRunnable spam = new SpamRunnable(spamMsg, control, delay);
        spam.execute();
    }
}
