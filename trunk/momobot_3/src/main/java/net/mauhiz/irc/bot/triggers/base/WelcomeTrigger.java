package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.trigger.IJoinTrigger;

/**
 * @author mauhiz
 */
public class WelcomeTrigger implements IJoinTrigger {
    /**
     * @see net.mauhiz.irc.base.trigger.IJoinTrigger#doTrigger(Join, IIrcControl)
     */
    @Override
    public void doTrigger(Join im, IIrcControl control) {
        Mask mask = new Mask(im.getFrom());
        IrcUser joiner = im.getServer().findUser(mask, true);
        if (!joiner.equals(im.getServer().getMyself())) {
            Notice notice = Notice.buildAnswer(im, "Bienvenue sur " + im.getChan());
            control.sendMsg(notice);
        }
    }
}
