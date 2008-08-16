package net.mauhiz.irc.bot.triggers.fun;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * Un trigger pour lancer les dés !
 * 
 * @author mauhiz
 */
public class RollTheDiceTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public RollTheDiceTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        IrcUser user = Users.getInstance(im.getServer()).findUser(new Mask(im.getFrom()), true);
        int max = 100;
        int number = (int) (Math.random() * max + 1);
        
        Privmsg msg = Privmsg.buildAnswer(im, user + " a obtenu un " + number + " (sur " + max + ')');
        control.sendMsg(msg);
        
    }
}
