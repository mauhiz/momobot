package net.mauhiz.board.gui.remote;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.mauhiz.board.Move;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Ctcp;

public class BoardManager {

    static class IrcOpponent {
        IIrcControl control;
        IrcUser opponentUser;
        IrcServer server;
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

    protected void sendMove(RemoteBoardAdapter rba, Move move) {
        String gameId = findGame(rba);

        if (gameId != null) {
            String text = move.toString();

            for (IrcOpponent opponent : opponents.get(gameId)) {
                Ctcp msg = new CtcpMove(null, opponent.opponentUser.getNick(), opponent.server, text);
                opponent.control.sendMsg(msg);
            }
        }
    }

}
