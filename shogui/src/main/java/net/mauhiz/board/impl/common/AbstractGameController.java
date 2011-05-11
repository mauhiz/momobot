package net.mauhiz.board.impl.common;

import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

public abstract class AbstractGameController implements GameController {
    private final BoardIO boardIO;
    protected Game game;

    //	private Map<PlayerType, Player> players = new TreeMap<PlayerType, Player>();

    protected AbstractGameController(BoardIO display) {
        super();
        boardIO = display;
        game = newGame();
    }

    public BoardIO getBoardIO() {
        return boardIO;
    }

    @Override
    public Game getGame() {
        return game;
    }

    protected abstract Game newGame();

    public void receiveMove(Move move) {
        game.applyMove(move);
    }
}
