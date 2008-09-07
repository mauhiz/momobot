package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class GatherTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public GatherTrigger(final String trigger) {
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
            IrcUser user = server.findUser(new Mask(cme.getFrom()), true);
            respMsg = "Gather lancé par " + user;
            Gather gather;
            String arg = getArgs(cme.getMessage());
            if (StringUtils.isNumeric(arg)) {
                int nbPlayers = Integer.parseInt(arg);
                if (nbPlayers > 0) {
                    gather = new Gather(chan, nbPlayers);
                } else {
                    gather = new Gather(chan);
                }
            } else {
                gather = new Gather(chan);
            }
            gather.add(user);
        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
