package net.mauhiz.board.model.data;

public interface Game {
	/**
	 * @return The player whose turn is next, or <code>null</code> if the move was rejected
	 */
	PlayerType applyMove(Move move);

	Board getBoard(int i);

	Board getLastBoard();

	Move getLastMove();

	Move getMove(int i);

	Iterable<Move> getMoves();

	Rule getRule();

	PlayerType getTurn();
}
