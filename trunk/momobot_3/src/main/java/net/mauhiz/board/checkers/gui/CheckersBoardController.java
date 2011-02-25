package net.mauhiz.board.checkers.gui;

import net.mauhiz.board.MoveReader;
import net.mauhiz.board.Square;
import net.mauhiz.board.checkers.model.CheckersBoard;
import net.mauhiz.board.checkers.model.CheckersMove;
import net.mauhiz.board.checkers.model.CheckersOwnedPiece;
import net.mauhiz.board.checkers.model.CheckersRule;
import net.mauhiz.board.gui.GuiBoardController;

import org.apache.commons.lang.NotImplementedException;

public class CheckersBoardController extends GuiBoardController<CheckersBoard, CheckersMove> {

    public CheckersBoardController(ICheckersGui display) {
        super(display, new CheckersBoard());
    }

    @Override
    protected ICheckersGui getDisplay() {
        return (ICheckersGui) super.getDisplay();
    }

    @Override
    public void movePiece(final Square to) {
        if (selectedSquare == null) {
            return;
        }

        if (board.move(board.getTurn(), selectedSquare, to)) {
            CheckersOwnedPiece currentPiece = getBoard().getOwnedPieceAt(to);

            if (CheckersRule.canPromote(currentPiece, to)) {
                currentPiece.setPromoted(true);
            }
            selectedSquare = null;
            refresh();
        }
    }

    @Override
    public MoveReader<CheckersBoard, CheckersMove> newMoveReader() {
        throw new NotImplementedException();
    }

    @Override
    public synchronized void refresh() {
        for (Square square : getSquares()) {
            CheckersOwnedPiece op = getBoard().getOwnedPieceAt(square);
            getDisplay().disableSquare(square);

            if (selectedSquare != null) { // from the board
                // available destinations
                CheckersOwnedPiece selected = getBoard().getOwnedPieceAt(selectedSquare);

                if (selected != null && !square.equals(selectedSquare)
                        && CheckersRule.canGo(board, selected, selectedSquare, square)) {
                    getDisplay().addMoveAction(square, this);
                }
            } else {
                // available pieces
                if (op != null && op.getPlayer() == board.getTurn()) {
                    getDisplay().addSelectAction(square, this);
                }
            }

            getDisplay().decorate(square, op);
        }

        if (selectedSquare != null) {
            getDisplay().addCancelAction(selectedSquare, this);
        }

        getDisplay().refresh();
    }
}
