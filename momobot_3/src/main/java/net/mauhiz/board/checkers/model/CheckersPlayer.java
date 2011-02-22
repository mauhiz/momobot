package net.mauhiz.board.checkers.model;

import net.mauhiz.board.Player;

public enum CheckersPlayer implements Player {
    BLACK, WHITE;

    public CheckersPlayer next() {
        return this == BLACK ? WHITE : BLACK;
    }
}
