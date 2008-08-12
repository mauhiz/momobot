package net.mauhiz.irc.bot.triggers.base;

import java.util.Map;

import net.mauhiz.irc.SqlUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class KnownPlayersTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public KnownPlayersTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg pme, final IIrcControl control) {
        Privmsg retour = Privmsg.buildPrivateAnswer(pme, "Je connais" + SqlUtils.countPlayers() + " joueur(s).");
        control.sendMsg(retour);
        for (final Map.Entry<String, String> item : SqlUtils.getPlayers()) {
            retour = Privmsg.buildPrivateAnswer(pme, item.getValue() + " - " + item.getKey());
            control.sendMsg(retour);
        }
    }
}
