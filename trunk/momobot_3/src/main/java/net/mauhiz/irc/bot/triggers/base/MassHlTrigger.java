package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */

public class MassHlTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(MassHlTrigger.class);
    
    /**
     * @param trigger
     *            le trigger
     */
    public MassHlTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        IrcServer server = cme.getServer();
        Channel chan = Channels.get(server).getChannel(cme.getTo());
        if (chan == null) {
            /* msg pv */
            return;
        }
        if (chan.isEmpty()) {
            LOG.error("no user on channel " + cme.getTo());
            return;
        }
        
        LOG.debug("MassHlTrigger : " + chan + " has " + chan.size() + " users");
        final StrBuilder msg = new StrBuilder("nudges");
        IrcUser from = Users.get(server).findUser(cme.getFrom(), true);
        for (final IrcUser nextIrcUser : chan) {
            if (nextIrcUser instanceof QnetUser && ((QnetUser) nextIrcUser).isService()) {
                /* no bots */
                LOG.debug("skipping bot : " + nextIrcUser);
                continue;
            } else if (nextIrcUser.equals(from)) {
                /* no caller */
                LOG.debug("skipping caller : " + nextIrcUser);
                continue;
            } else if (nextIrcUser.getNick().equalsIgnoreCase(server.getMyNick())) {
                /* no self */
                LOG.debug("skipping myself : " + nextIrcUser);
                continue;
            }
            LOG.debug("appending : " + nextIrcUser);
            msg.append(' ').append(nextIrcUser);
        }
        
        // MomoBot.getBotInstance().sendAction(chan, msg.toString());
        /* FIXME action */
    }
}
