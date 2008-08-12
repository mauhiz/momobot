package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.model.WhoisRequest;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IJoinTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class WhoisTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IJoinTrigger {
    
    /**
     * @param trigger
     *            le trigger
     */
    public WhoisTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * j'évite de flooder en joinant clanwar!
     * 
     * @see net.mauhiz.irc.bot.triggers.IJoinTrigger#doTrigger(net.mauhiz.irc.base.msg.Join,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Join im, final IIrcControl control) {
        // if (MomoBot.AUTOJOIN.contains(im.getChan().toLowerCase(Locale.US))) {
        new WhoisRequest(im.getFrom(), im.getServer(), control).execute();
        // }
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg pme, final IIrcControl control) {
        new WhoisRequest(getArgs(pme.getMessage()), pme.getServer(), control, pme.getFrom()).execute();
    }
}
