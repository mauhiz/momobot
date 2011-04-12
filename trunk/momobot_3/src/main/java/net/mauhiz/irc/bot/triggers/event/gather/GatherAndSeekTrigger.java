package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.fake.FakeUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.GatherAndSeek;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

/**
 * @author Topper
 */
public class GatherAndSeekTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public GatherAndSeekTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        IrcChannel chan = (IrcChannel) cme.getTo();
        ChannelEvent evt = chan.getEvt();
        String respMsg;
        if (evt == null) {

            respMsg = "Erreur : L'argument qui suit doit etre un chiffre entre 1 et 5";
            try {
                int nbPlayers = Integer.parseInt(getArgs(cme.getMessage()));
                if (nbPlayers > 0 && nbPlayers < 5) {
                    IrcUser user = (IrcUser) cme.getFrom();
                    respMsg = "Gather lance par " + user.getNick();
                    GatherAndSeek gath = new GatherAndSeek(chan, nbPlayers);
                    for (int i = 0; i < nbPlayers; i++) {
                        IrcUser ircuser = new FakeUser("P" + (i + 1));
                        gath.add(ircuser);
                    }
                }
            } catch (NumberFormatException e) {
                // invalid arg
            }

        } else {
            respMsg = "Un " + evt.getClass().getSimpleName() + " est deja lance sur " + cme.getTo();

        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
