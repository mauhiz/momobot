package net.mauhiz.board.checkers.model;

import net.mauhiz.board.Piece;

public enum CheckersPiece implements Piece {
    PAWN;

    public String getName() {
        return "O";
    }
}
