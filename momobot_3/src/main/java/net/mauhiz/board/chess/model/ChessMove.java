package net.mauhiz.board.chess.model;

import net.mauhiz.board.AbstractMove;
import net.mauhiz.board.chess.PgnAdapter;
import net.mauhiz.board.chess.model.ChessBoard.Status;

public class ChessMove extends AbstractMove<ChessBoard> {
    private boolean capture;
    public ChessPiece moved;
    public ChessPiece promotion;
    public Status status;

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    @Override
    public String toString(ChessBoard board) {
        return PgnAdapter.toPgn(board, this);
    }
}
