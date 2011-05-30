package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public interface BoardIO {
	PlayerType sendMove(Move move);
}
