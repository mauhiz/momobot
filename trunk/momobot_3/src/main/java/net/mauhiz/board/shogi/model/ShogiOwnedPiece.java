package net.mauhiz.board.shogi.model;

import net.mauhiz.board.OwnedPiece;

public class ShogiOwnedPiece implements OwnedPiece {

    private final ShogiPiece piece;
    private ShogiPlayer player;
    private boolean promoted;

    public ShogiOwnedPiece(ShogiPlayer player, ShogiPiece piece) {
        this.piece = piece;
        this.player = player;
    }

    public ShogiPiece getPiece() {
        return piece;
    }

    public ShogiPlayer getPlayer() {
        return player;
    }

    public String getSymbol() {
        return promoted ? piece.getName() : piece.getPromotedKanji();
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPlayer(ShogiPlayer player) {
        this.player = player;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }
}
