package net.mauhiz.board.checkers.gui;

import net.mauhiz.board.Square;
import net.mauhiz.board.checkers.model.CheckersBoard;
import net.mauhiz.board.checkers.model.CheckersOwnedPiece;
import net.mauhiz.board.checkers.model.CheckersRule;
import net.mauhiz.board.gui.BoardController;

public class CheckersBoardController extends BoardController {

    public CheckersBoardController(ICheckersGui display) {
        super();
        board = new CheckersBoard();
        this.display = display;
    }

    @Override
    protected void clear() {
        getDisplay().clear();
    }

    @Override
    public CheckersBoard getBoard() {
        return (CheckersBoard) board;
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

    @Override
    public void selectPiece(Square at) {
        selectedSquare = at;
        refresh();
    }
}
