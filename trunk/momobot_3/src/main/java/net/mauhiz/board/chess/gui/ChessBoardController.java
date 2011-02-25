package net.mauhiz.board.chess.gui;

import net.mauhiz.board.MoveReader;
import net.mauhiz.board.Square;
import net.mauhiz.board.chess.PgnAdapter;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessMove;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessRule;
import net.mauhiz.board.gui.GuiBoardController;

public class ChessBoardController extends GuiBoardController<ChessBoard, ChessMove> {
    public ChessBoardController(IChessGui display) {
        super(display, new ChessBoard());
    }

    @Override
    protected IChessGui getDisplay() {
        return (IChessGui) super.getDisplay();
    }

    @Override
    public void movePiece(final Square to) {
        if (selectedSquare == null) {
            return;
        }

        if (getBoard().move(getBoard().getTurn(), selectedSquare, to)) {
            ChessOwnedPiece currentPiece = getBoard().getOwnedPieceAt(to);

            if (ChessRule.canPromote(currentPiece, to)) {
                ChessPiece[] promotions = { ChessPiece.QUEEN, ChessPiece.ROOK, ChessPiece.BISHOP, ChessPiece.KNIGHT };
                getDisplay().showPromotionDialog(promotions, this, to);

            }
            selectedSquare = null;
            refresh();
        }
    }

    @Override
    public MoveReader<ChessBoard, ChessMove> newMoveReader() {
        return new PgnAdapter();
    }

    public void promote(Square to, ChessPiece promotion) {
        getBoard().promote(to, promotion);
    }

    @Override
    public synchronized void refresh() {
        for (Square square : getSquares()) {
            ChessOwnedPiece op = getBoard().getOwnedPieceAt(square);
            getDisplay().disableSquare(square);

            if (selectedSquare != null) { // from the board
                // available destinations
                ChessOwnedPiece selected = getBoard().getOwnedPieceAt(selectedSquare);

                if (selected != null && !square.equals(selectedSquare)
                        && ChessRule.canGo(getBoard(), selected, selectedSquare, square)) {
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
