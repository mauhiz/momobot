package net.mauhiz.board.gui;

import java.awt.Color;
import java.awt.Dimension;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;

public interface IBoardGui<B extends Board, M extends Move<B>> {
    void addCancelAction(Square square, GuiBoardController<B, M> controller);

    void addMoveAction(Square square, GuiBoardController<B, M> controller);

    void addSelectAction(Square square, GuiBoardController<B, M> controller);

    void afterInit();

    void appendSquare(Square square, Dimension size);

    void clear();

    void close();

    void disableSquare(Square square);

    Dimension getDefaultSize();

    Dimension getMinimumSize();

    Color getSquareBgcolor(Square square);

    void initDisplay();

    void initLayout(Dimension size);

    void newGame(IBoardController<B, M> controller);

    void refresh();
}
