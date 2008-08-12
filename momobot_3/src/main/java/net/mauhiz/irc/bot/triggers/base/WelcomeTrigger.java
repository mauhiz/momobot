package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.bot.triggers.IJoinTrigger;

/**
 * @author mauhiz
 */
public class WelcomeTrigger implements IJoinTrigger {
    /**
     * @see net.mauhiz.irc.bot.triggers.IJoinTrigger#doTrigger(net.mauhiz.irc.base.msg.Join,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Join im, final IIrcControl control) {
        // if
        // (MomoBot.AUTOJOIN.contains(channel.getNom().toLowerCase(Locale.US)))
        // {
        Notice notice = Notice.buildAnswer(im, "Bienvenue sur " + im.getChan());
        control.sendMsg(notice);
        // }
    }
}
