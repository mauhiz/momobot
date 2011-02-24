package net.mauhiz.board.chess.model;

import net.mauhiz.board.Piece;

public enum ChessPiece implements Piece {
    BISHOP, KING, KNIGHT {
        @Override
        public String getName() {
            return "N";
        }
    },
    PAWN, QUEEN, ROOK;

    public static ChessPiece fromName(String name) {
        for (ChessPiece piece : values()) {
            if (piece.getName().equals(name)) {
                return piece;
            }
        }
        return null;
    }

    public String getName() {
        return name().substring(0, 1);
    }
}
