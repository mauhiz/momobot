package net.mauhiz.board.model.data;

import java.util.List;

public interface Game {
	Board getBoard();

	List<Move> getMoves();

	Rule getRule();

	/**
	 * @return The player whose turn is next, or <code>null</code> if the move was rejected
	 */
	PlayerType applyMove(Move move);

	PlayerType getTurn();
}
