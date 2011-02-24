package net.mauhiz.board.gui;

import java.awt.Color;
import java.awt.Dimension;

import net.mauhiz.board.Square;

public interface IBoardGui {
    void addCancelAction(Square square, BoardController controller);

    void addMoveAction(Square square, BoardController controller);

    void addSelectAction(Square square, BoardController controller);

    void afterInit();

    void appendSquare(Square square, Dimension size);

    void clear();

    void disableSquare(Square square);

    Dimension getDefaultSize();

    Dimension getMinimumSize();

    Color getSquareBgcolor(Square square);

    void initDisplay();

    void initLayout(Dimension size);

    void newGame();

    void refresh();
}
