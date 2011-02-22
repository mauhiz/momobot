package net.mauhiz.board.shogi.model;

import net.mauhiz.board.Player;

public enum ShogiPlayer implements Player {
    BOTTOM, TOP;

    public ShogiPlayer next() {
        return this == BOTTOM ? TOP : BOTTOM;
    }
}
