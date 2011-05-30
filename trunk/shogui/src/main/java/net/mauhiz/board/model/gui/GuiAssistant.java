package net.mauhiz.board.model.gui;

import java.awt.Dimension;

import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.util.IAction;

public interface GuiAssistant {

	void appendSquares(Iterable<Square> squares, Dimension size);

	void clear();

	void close();

	void decorate(Square square, Piece piece);

	void disableSquare(Square square);

	void enableSquare(Square square, IAction cancelAction);

	void initDisplay();

	void initLayout(Dimension sizeInSquares);

	void refreshBoard();

	void start();
}
