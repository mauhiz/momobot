package net.mauhiz.board.model.data;

import java.awt.Dimension;

public interface Board {
	void applyMove(Move move);

	Board copy();

	Piece getPieceAt(Square square);

	Dimension getSize();

	Iterable<Square> getSquares();

	/**
	 * @return The piece that was previously there
	 */
	Piece setPieceAt(Square square, Piece piece);
}
