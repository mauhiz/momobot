package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.model.data.PlayerType;

public enum ShogiPlayerType implements PlayerType {
	SENTE, GOTE;

	@Override
	public PlayerType other() {
		return this == SENTE ? GOTE : SENTE;
	}
}
