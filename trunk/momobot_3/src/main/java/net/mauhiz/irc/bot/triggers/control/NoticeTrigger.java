package net.mauhiz.irc.bot.triggers.control;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;

/**
 * @author mauhiz
 */
public class NoticeTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public NoticeTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String args = getArgs(im.getMessage());
        int index = args.indexOf(' ');
        if (index < 1) {
            Privmsg msg = Privmsg.buildPrivateAnswer(im, "pas assez de parametres. " + getTriggerHelp());
            control.sendMsg(msg);
        } else {
            /* FIXME cross-server */
            Notice msg = new Notice(null, args.substring(0, index), im.getServer(), args.substring(index + 1));
            control.sendMsg(msg);
        }
    }
    
    @Override
    public String getTriggerHelp() {
        return "syntaxe $" + getTriggerText() + " target msg";
    }
}
