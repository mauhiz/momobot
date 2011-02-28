package net.mauhiz.board.gui.remote;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
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

    private static final BoardManager INSTANCE = new BoardManager();

    public static BoardManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, RemoteBoardAdapter<? extends Board<? extends Piece, ? extends Player>, ? extends Move>> games = new HashMap<String, RemoteBoardAdapter<? extends Board<? extends Piece, ? extends Player>, ? extends Move>>();

    private final Map<String, Set<IrcOpponent>> opponents = new HashMap<String, Set<IrcOpponent>>();

    private String findGame(RemoteBoardAdapter<? extends Board<? extends Piece, ? extends Player>, ? extends Move> rba) {
        for (Entry<String, RemoteBoardAdapter<? extends Board<? extends Piece, ? extends Player>, ? extends Move>> ent : games
                .entrySet()) {
            if (ent.getValue().equals(rba)) {
                return ent.getKey();
            }

        }
        return null;
    }

    public void receiveMove(String gameId, String moveStr) {
        RemoteBoardAdapter<? extends Board<? extends Piece, ? extends Player>, ? extends Move> rba = games.get(gameId);

        if (rba != null) {
            rba.readMove(moveStr);
        }
    }

    public <M extends Move> void sendMove(
            RemoteBoardAdapter<? extends Board<? extends Piece, ? extends Player>, ? extends Move> rba, M move) {
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
