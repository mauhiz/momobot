package net.mauhiz.board.model.gui;

import java.awt.Color;

import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.data.Square;

public interface BoardGui extends PanelGui, BoardIO {

	Color getSquareBgcolor(Square square);

	void newGame();
}
