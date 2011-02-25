package net.mauhiz.irc.bot.triggers.control;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;

/**
 * @author mauhiz
 */
public class SayTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public SayTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg pme, IIrcControl control) {
        String args = getArgs(pme.getMessage());
        int index = args.indexOf(' ');
        if (index < 1) {
            Privmsg msg = Privmsg.buildAnswer(pme, "pas assez de parametres. " + getTriggerHelp());
            control.sendMsg(msg);
        } else {
            /* TODO say cross-server ? */
            Privmsg msg = new Privmsg(null, args.substring(0, index), pme.getServer(), args.substring(index + 1));
            control.sendMsg(msg);
        }
    }
    
    @Override
    public String getTriggerHelp() {
        return "syntaxe $" + getTriggerText() + " target msg";
    }
}
