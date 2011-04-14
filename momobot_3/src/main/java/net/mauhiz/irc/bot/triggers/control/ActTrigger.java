package net.mauhiz.irc.bot.triggers.control;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Action;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;

/**
 * @author mauhiz
 */
public class ActTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ActTrigger(String trigger) {
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
            IrcUser target = im.getServerPeer().getNetwork().findUser(args.substring(0, index), false);
            Action msg = new Action(null, target, im.getServerPeer(), args.substring(index + 1));
            control.sendMsg(msg);
        }
    }

    @Override
    public String getTriggerHelp() {
        return "syntaxe $" + getTriggerText() + " target msg";
    }
}
