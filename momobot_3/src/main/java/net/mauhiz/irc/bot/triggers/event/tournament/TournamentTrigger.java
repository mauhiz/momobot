package net.mauhiz.irc.bot.triggers.event.tournament;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author Topper
 * 
 */
public class TournamentTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public TournamentTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        IrcServer server = cme.getServer();
        IrcChannel chan = server.findChannel(cme.getTo());
        ChannelEvent evt = chan.getEvt();
        String respMsg;
        if (evt != null) {
            respMsg = "Un " + evt.getClass().getSimpleName() + " est déjà lancé sur " + cme.getTo();
        } else {
            // ON crée le tn en fonction des paramètres
            // On pécho les params
            String[] params = getArgs(cme.getMessage()).split(" ");
            
            // Tcheck si les params st ok
            if (params.length != 0) {
                Tournament tn = new Tournament(chan, params);
                tn.generateTemplate();
                respMsg = tn.toString();
            } else {
                respMsg = "Paramètre(s) invalide(s)";
            }
            
            // new Gather(chan).add(user);
        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
