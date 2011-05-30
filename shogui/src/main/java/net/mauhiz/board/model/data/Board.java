package net.mauhiz.board.model.data;

import java.awt.Dimension;

public interface Board {
	void applyMove(Move move);

	Board copy();

	Square findSquare(PlayerType player, PieceType piece);

	Piece getPieceAt(Square square);

	Dimension getSize();

	Iterable<Square> getSquares();

	boolean isFriendlyPieceOn(PlayerType player, Square to);

	boolean isObstruction(Square from, Square to);

	/**
	 * @return The piece that was previously there
	 */
	Piece setPieceAt(Square square, Piece piece);
}
