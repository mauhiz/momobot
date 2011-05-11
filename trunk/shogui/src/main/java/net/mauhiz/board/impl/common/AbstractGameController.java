package net.mauhiz.board.impl.common;

import net.mauhiz.board.impl.common.data.NormalMoveImpl;
import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public abstract class AbstractGameController implements GameController {
	private final BoardIO boardIO;
	protected Game game;
//	private Map<PlayerType, Player> players = new TreeMap<PlayerType, Player>();

	protected AbstractGameController(BoardIO display) {
		super();
		this.boardIO = display;
		game = newGame();
	}

	@Override
	public Move generateMove(PlayerType player, Square from, Square to) {
		return new NormalMoveImpl(player, from, to);
	}

	public BoardIO getBoardIO() {
		return boardIO;
	}

	protected abstract Game newGame();
	
	@Override
	public Game getGame() {
		return game;
	}

	public void receiveMove(Move move) {
		game.applyMove(move);
	}
}
