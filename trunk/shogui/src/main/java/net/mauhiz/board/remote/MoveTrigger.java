package net.mauhiz.board.remote;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

public class MoveTrigger implements IPrivmsgTrigger {

    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        if (im instanceof CtcpMove) {
            String moveText = ((CtcpMove) im).getCtcpContent();
            String[] args = StringUtils.split(moveText, ' ');
            String gameId = args[0];
            String move = args[1];
            BoardManager.getInstance().receiveMove(gameId, move);
        }
    }

    @Override
    public boolean isActivatedBy(String text) {
        return true;
    }

}
