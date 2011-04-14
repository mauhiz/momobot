package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.WhoisRequest;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IJoinTrigger;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

/**
 * @author mauhiz
 */
public class WhoisTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IJoinTrigger {

    /**
     * @param trigger
     *            le trigger
     */
    public WhoisTrigger(String trigger) {
        super(trigger);
    }

    /**
     * j'evite de flooder en joinant clanwar!
     * 
     * @see net.mauhiz.irc.base.trigger.IJoinTrigger#doTrigger(Join, IIrcControl)
     */
    @Override
    public void doTrigger(Join im, IIrcControl control) {
        IrcUser user = (IrcUser) im.getFrom();
        WhoisRequest wr = new WhoisRequest(user.getNick(), im.getServerPeer(), control);
        wr.startAs("Whois Request");
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg pme, IIrcControl control) {
        WhoisRequest wr = new WhoisRequest(getArgs(pme.getMessage()), pme.getServerPeer(), control);
        wr.setReportTo(pme.getFrom());
        wr.startAs("Whois Request");
    }
}
