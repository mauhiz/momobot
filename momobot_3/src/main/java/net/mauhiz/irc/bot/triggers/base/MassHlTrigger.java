package net.mauhiz.irc.bot.triggers.base;

import java.util.Set;
import java.util.TreeSet;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Action;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */

public class MassHlTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    
    /**
     * @param trigger
     *            le trigger
     */
    public MassHlTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        IrcServer server = cme.getServer();
        IrcChannel chan = server.findChannel(cme.getTo());
        if (chan == null) {
            /* msg pv */
            return;
        }
        if (chan.size() == 0) {
            LOG.error("no user on channel " + cme.getTo());
            return;
        }
        
        LOG.debug("MassHlTrigger : " + chan.fullName() + " has " + chan.size() + " users");
        IrcUser from = server.findUser(new Mask(cme.getFrom()), true);
        Set<IrcUser> nudgeableUsers = new TreeSet<IrcUser>();
        for (IrcUser nextIrcUser : chan) {
            if (nextIrcUser.isService()) {
                /* no bots */
                LOG.debug("skipping bot : " + nextIrcUser.getNick());
                continue;
            } else if (nextIrcUser.equals(from)) {
                /* no caller */
                LOG.debug("skipping caller : " + nextIrcUser.getNick());
                continue;
            } else if (nextIrcUser.equals(server.getMyself())) {
                /* no self */
                LOG.debug("skipping myself : " + nextIrcUser.getNick());
                continue;
            }
            nudgeableUsers.add(nextIrcUser);
        }
        
        if (nudgeableUsers.isEmpty()) {
            return;
        }
        StringBuilder msg = new StringBuilder();
        msg.append("nudges");
        for (IrcUser user : nudgeableUsers) {
            LOG.debug("appending : " + user.getNick());
            msg.append(' ');
            msg.append(user.getNick());
        }
        
        Action pmsg = Action.buildAnswer(cme, msg.toString());
        control.sendMsg(pmsg);
    }
}
