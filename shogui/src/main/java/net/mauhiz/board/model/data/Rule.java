package net.mauhiz.board.model.data;

import java.util.List;

public interface Rule {
	Board newBoard();

	PlayerType[] getPlayerTypes();
	
	boolean isValid(Move move, Board board, List<Move> history);

	void initPieces(Board board);

	PlayerType getStartingPlayer();
}
