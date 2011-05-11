package net.mauhiz.board.model.gui;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.Square;

public interface InteractiveBoardGui extends BoardGui {

	void disableSquare(Square square);

	void addCancelAction(Square square);

	void addMoveAction(Square square, Move move);

	void addSelectAction(Square square);

	/**
	 * Selects a square on the board
	 * @param at
	 */
	void selectSquare(Square at);

	Square getSelectedSquare();

	void cancelSelection();
}
