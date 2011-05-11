package net.mauhiz.board.impl.go.data;

import net.mauhiz.board.model.data.PieceType;

public enum GoPieceType implements PieceType {
    STONE;

    public String getName() {
        return "O";
    }

    @Override
    public String toString() {
        return getName();
    }
}
