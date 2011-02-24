package net.mauhiz.board.chess.model;

import net.mauhiz.board.Move;
import net.mauhiz.board.chess.model.ChessBoard.Status;

public class ChessMove extends Move {
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
}
