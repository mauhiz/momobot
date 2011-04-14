package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class MoveAction<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends AbstractAction {

    private final IGuiBoardController<B, M> controller;
    private final Square to;

    public MoveAction(IGuiBoardController<B, M> controller, Square to) {
        super();
        this.to = to;
        this.controller = controller;
    }

    @Override
    protected void doAction() {
        controller.movePiece(to);
    }

    @Override
    protected boolean isAsynchronous() {
        return true; // this might take time, especially if communicating the move
    }
}
