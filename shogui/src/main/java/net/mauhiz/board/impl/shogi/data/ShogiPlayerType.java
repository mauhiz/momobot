package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.model.data.PlayerType;

public enum ShogiPlayerType implements PlayerType {
	GOTE, SENTE;

	public ShogiPlayerType other() {
		return this == SENTE ? GOTE : SENTE;
	}
}
