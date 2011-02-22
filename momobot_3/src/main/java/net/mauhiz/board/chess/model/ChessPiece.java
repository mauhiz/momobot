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

    public String getName() {
        return name().substring(0, 1);
    }
}
