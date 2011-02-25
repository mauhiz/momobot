package net.mauhiz.board.gui;

import java.awt.Color;
import java.awt.Dimension;

import net.mauhiz.board.Square;

public abstract class AbstractBoardGui implements IBoardGui {

    public Dimension getDefaultSize() {
        return new Dimension(400, 400);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    public Color getSquareBgcolor(Square square) {
        return (square.x + square.y) % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }

    protected abstract String getWindowTitle();

    protected abstract BoardController newController();

    public void newGame(BoardController controller) {
        controller.init();
    }

}
