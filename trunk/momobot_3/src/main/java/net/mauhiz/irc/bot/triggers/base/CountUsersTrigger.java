package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class CountUsersTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public CountUsersTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        /* TODO cross server */
        Privmsg retour = Privmsg.buildPrivateAnswer(im, "Je connais " + im.getServer().countUsers()
                + " utilisateur(s).");
        control.sendMsg(retour);
        
    }
}
