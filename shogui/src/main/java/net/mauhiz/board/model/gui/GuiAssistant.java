package net.mauhiz.board.model.gui;

import java.awt.Dimension;

import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.util.IAction;

public interface GuiAssistant {

	void enableSquare(Square square, IAction cancelAction);

	void appendSquare(Square square, Dimension size);

	void decorate(Square square, Piece piece);

	void initLayout(Dimension sizeInSquares);

	void close();

	void disableSquare(Square square);

	void start();

	void refresh();

	void initDisplay();
}
