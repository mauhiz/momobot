package net.mauhiz.board.gui;

import java.awt.Dimension;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public abstract class AbstractBoardController<B extends Board<? extends Piece, ? extends Player>, M extends Move>
        implements IBoardController<B, M> {

    protected final B board;

    public AbstractBoardController(B board) {
        this.board = board;
    }

    public B getBoard() {
        return board;
    }

    protected Dimension getBoardSize() {
        return board.getSize();
    }

    protected Iterable<Square> getSquares() {
        return new SquareView(getBoardSize());
    }

}
