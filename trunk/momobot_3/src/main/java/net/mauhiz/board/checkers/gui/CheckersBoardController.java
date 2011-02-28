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
        getBoard().setRule(new CheckersRule());
    }

    @Override
    protected ICheckersGui getDisplay() {
        return super.getDisplay();
    }

    @Override
    public void movePiece(final Square to) {
        if (selectedSquare == null) {
            return;
        }
        CheckersMove move = new CheckersMove();
        move.setFrom(selectedSquare);
        move.setTo(to);
        if (getBoard().move(move)) {
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
                        && CheckersRule.canGo(getBoard(), selectedSquare, square)) {
                    getDisplay().addMoveAction(square, this);
                }
            } else {
                // available pieces
                if (op != null && op.getPlayer() == getBoard().getTurn()) {
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
