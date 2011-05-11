package net.mauhiz.board.model.data;

public interface Rule {
	Board newBoard();

	PlayerType[] getPlayerTypes();

	boolean isValid(Move move, Game game);

	void initPieces(Board board);

	PlayerType getStartingPlayer();

	Move generateMove(Square from, Square to, Game game);
}
