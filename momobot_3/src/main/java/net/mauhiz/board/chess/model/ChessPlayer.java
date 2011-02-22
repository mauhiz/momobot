package net.mauhiz.board.chess.model;

import net.mauhiz.board.Player;

public enum ChessPlayer implements Player {
    BLACK, WHITE;

    public ChessPlayer next() {
        return this == BLACK ? WHITE : BLACK;
    }
}
