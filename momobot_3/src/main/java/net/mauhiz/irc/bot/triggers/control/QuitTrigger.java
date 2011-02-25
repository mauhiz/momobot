package net.mauhiz.irc.bot.triggers.control;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class QuitTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public QuitTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String args = getArgs(im.getMessage());
        /* TODO quit cross servers */
        Quit quit;
        if (StringUtils.isEmpty(args)) {
            IrcUser from = im.getServer().findUser(new Mask(im.getFrom()), true);
            quit = new Quit(im.getServer(), "Requested by " + from);
        } else {
            quit = new Quit(im.getServer(), args);
        }
        control.sendMsg(quit);
    }
}
