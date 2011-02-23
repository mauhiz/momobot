package net.mauhiz.board.checkers.model;

import net.mauhiz.board.OwnedPiece;

public class CheckersOwnedPiece implements OwnedPiece {

    private final CheckersPiece piece;
    private CheckersPlayer player;
    private boolean promoted;

    public CheckersOwnedPiece(CheckersPlayer player, CheckersPiece piece) {
        this.piece = piece;
        this.player = player;
    }

    public CheckersPiece getPiece() {
        return piece;
    }

    public CheckersPlayer getPlayer() {
        return player;
    }

    public String getSymbol() {
        return promoted ? "8" : piece.getName();
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPlayer(CheckersPlayer player) {
        this.player = player;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }
}
