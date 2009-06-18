package net.mauhiz.irc.bot.triggers.base;

import java.util.Map.Entry;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.cs.PlayerDB;

/**
 * @author mauhiz
 */
public class KnownPlayersTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public KnownPlayersTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg pme, IIrcControl control) {
        Privmsg retour = Privmsg.buildPrivateAnswer(pme, "Je connais" + PlayerDB.countPlayers() + " joueur(s).");
        control.sendMsg(retour);
        for (Entry<String, String> item : PlayerDB.getPlayers()) {
            retour = Privmsg.buildPrivateAnswer(pme, item.getValue() + " - " + item.getKey());
            control.sendMsg(retour);
        }
    }
}
