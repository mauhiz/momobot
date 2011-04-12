package net.mauhiz.irc.bot.triggers.event.tournament;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

/**
 * @author Topper
 * 
 */
public class TournamentTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public TournamentTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        IrcChannel chan = (IrcChannel) cme.getTo();
        ChannelEvent evt = chan.getEvt();
        String respMsg;
        if (evt == null) {
            // ON cree le tn en fonction des parametres
            // On pecho les params
            String[] params = getArgs(cme.getMessage()).split(" ");

            // Tcheck si les params st ok
            if (params.length == 0) {
                respMsg = "Parametre(s) invalide(s)";
            } else {
                Tournament tn = new Tournament(chan, params);
                tn.generateTemplate();
                respMsg = tn.toString();
            }

            // new Gather(chan).add(user);
        } else {
            respMsg = "Un " + evt.getClass().getSimpleName() + " est deja lance sur " + cme.getTo();
        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
