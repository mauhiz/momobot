package net.mauhiz.board.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Ctcp;

public class BoardManager {

	static class IrcOpponent {
		IIrcControl control;
		IrcUser opponentUser;
		IIrcServerPeer server;
	}

	private static final BoardManager INSTANCE = new BoardManager();

	public static BoardManager getInstance() {
		return INSTANCE;
	}

	private final Map<String, RemoteBoardAdapter> games = new HashMap<String, RemoteBoardAdapter>();
	private final Map<String, Set<IrcOpponent>> opponents = new HashMap<String, Set<IrcOpponent>>();

	private String findGame(RemoteBoardAdapter rba) {
		for (Entry<String, RemoteBoardAdapter> ent : games.entrySet()) {
			if (ent.getValue().equals(rba)) {
				return ent.getKey();
			}

		}
		return null;
	}

	public void receiveMove(String gameId, String moveStr) throws IOException {
		RemoteBoardAdapter rba = games.get(gameId);

		if (rba != null) {
			rba.readMove(moveStr);
		}
	}

	public void sendMove(RemoteBoardAdapter rba, Move move) {
		String gameId = findGame(rba);

		if (gameId != null) {
			String text = move.toString();

			for (IrcOpponent opponent : opponents.get(gameId)) {
				Ctcp msg = new CtcpMove(null, opponent.opponentUser, opponent.server, text);
				opponent.control.sendMsg(msg);
			}
		}
	}
}
