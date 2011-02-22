package net.mauhiz.board.chess.model;

import net.mauhiz.board.OwnedPiece;

public class ChessOwnedPiece implements OwnedPiece {

    private ChessPiece piece;
    private final ChessPlayer player;

    public ChessOwnedPiece(ChessPlayer player, ChessPiece piece) {
        this.piece = piece;
        this.player = player;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public ChessPlayer getPlayer() {
        return player;
    }

    public String getSymbol() {
        return piece.getName();
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }
}
