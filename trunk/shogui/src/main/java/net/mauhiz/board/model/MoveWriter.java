package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

public interface MoveWriter {
    String write(Game game, Move move);
}
