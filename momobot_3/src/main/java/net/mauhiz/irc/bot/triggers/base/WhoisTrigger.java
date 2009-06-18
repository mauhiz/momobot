package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
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
    public WhoisTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * j'evite de flooder en joinant clanwar!
     * 
     * @see net.mauhiz.irc.bot.triggers.IJoinTrigger#doTrigger(Join, IIrcControl)
     */
    @Override
    public void doTrigger(Join im, IIrcControl control) {
        // if (MomoBot.AUTOJOIN.contains(im.getChan().toLowerCase(Locale.US))) {
        IrcUser user = im.getServer().findUser(new Mask(im.getFrom()), true);
        new WhoisRequest(user.getNick(), im.getServer(), control).startAs("Whois Request");
        // }
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg pme, IIrcControl control) {
        new WhoisRequest(getArgs(pme.getMessage()), pme.getServer(), control, pme.getFrom()).startAs("Whois Request");
    }
}
