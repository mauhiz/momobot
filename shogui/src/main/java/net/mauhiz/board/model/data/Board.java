package net.mauhiz.board.model.data;

import java.awt.Dimension;

public interface Board {
	Iterable<Square> getSquares();

	Piece getPieceAt(Square square);
	
	/**
	 * @return The piece that was previously there
	 */
	Piece setPieceAt(Square square, Piece piece);
	
	Dimension getSize();

	void applyMove(Move move);
}
