package net.mauhiz.irc.bot.triggers.cs;

import net.mauhiz.irc.NetUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class TopFraggerTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public TopFraggerTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        String args = getArgs(im.getMessage());
        Server server = new Server(NetUtils.makeISA(args));
        server.getDetails();
        Player best = null;
        int bestScore = Integer.MIN_VALUE;
        for (Player player : server.getPlayers().values()) {
            if (player.getFrags() > bestScore) {
                best = player;
            }
        }
        String reply;
        if (best == null) {
            reply = "There is no player on this server";
        } else {
            reply = "Best fragger on " + server.getName() + " : " + best.getName() + " with " + best.getFrags()
                    + " frags";
        }
        
        Privmsg msg = Privmsg.buildAnswer(im, reply);
        control.sendMsg(msg);
    }
}
