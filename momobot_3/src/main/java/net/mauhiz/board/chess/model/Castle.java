package net.mauhiz.board.chess.model;

public class Castle extends ChessMove {
    @Override
    public boolean isCapture() {
        return false;
    }

    public boolean isGreat() {
        return to.x < from.x;
    }
}
