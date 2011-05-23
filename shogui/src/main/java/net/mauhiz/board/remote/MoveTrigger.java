package net.mauhiz.board.remote;

import java.io.IOException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

public class MoveTrigger implements IPrivmsgTrigger {

	public void doTrigger(Privmsg im, IIrcControl control) {
		if (im instanceof CtcpMove) {
			String moveText = ((CtcpMove) im).getCtcpContent();
			String[] args = StringUtils.split(moveText, ' ');
			String gameId = args[0];
			String move = args[1];
			try {
				BoardManager.getInstance().receiveMove(gameId, move);
			} catch (IOException ioe) {
				Privmsg ans = new Privmsg(im, "Failed to receive move: " + ioe);
				control.sendMsg(ans);
			}
		}
	}

	public boolean isActivatedBy(String text) {
		return true;
	}

}
