package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

public interface MoveReader {
    Move read(Game game, String moveStr);
}
