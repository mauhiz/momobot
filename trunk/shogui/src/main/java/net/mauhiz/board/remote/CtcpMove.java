package net.mauhiz.board.remote;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.Ctcp;

public class CtcpMove extends Ctcp {

	public CtcpMove(IIrcServerPeer server, Target from, Target to, Move move) {
		super(server, from, to, move.toString());
	}

	@Override
	protected String getCommand() {
		return "MOVE";
	}

}
