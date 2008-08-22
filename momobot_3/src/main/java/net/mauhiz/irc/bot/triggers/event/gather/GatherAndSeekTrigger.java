package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.GatherAndSeek;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

public class GatherAndSeekTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public GatherAndSeekTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        IrcServer server = cme.getServer();
        Channel chan = Channels.getInstance(server).get(cme.getTo());
        ChannelEvent evt = chan.getEvt();
        String respMsg = "";
        if (evt != null) {
            respMsg = "Un " + evt.getClass().getSimpleName() + " est déjà lancé sur " + cme.getTo();
        } else {
            
            try {
                int nbPlayers = Integer.parseInt(getArgs(cme.getMessage()));
                if (nbPlayers > 0 && nbPlayers < 6) {
                    IrcUser user = Users.getInstance(server).findUser(new Mask(cme.getFrom()), true);
                    respMsg = "Gather lancé par " + user;
                    GatherAndSeek gath = new GatherAndSeek(chan);
                    for (int i = 0; i < nbPlayers; i++) {
                        IrcUser ircuser = new IrcUser("P" + (i + 1));
                        gath.add(ircuser);
                    }
                    
                }
            } catch (NumberFormatException e) {
                respMsg = "Erreur : L'argument qui suit doit etre un chiffre entre 1 et 5";
                
            }
            
        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
