package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class CancelAction<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends AbstractAction {

    private final IGuiBoardController<B, M> gui;

    public CancelAction(IGuiBoardController<B, M> gui) {
        super();
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.cancelSelection();
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // should be immediate
    }
}
