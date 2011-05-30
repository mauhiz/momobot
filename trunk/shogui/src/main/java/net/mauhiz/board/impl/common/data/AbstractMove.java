package net.mauhiz.board.impl.common.data;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public class AbstractMove implements Move {
	private final PlayerType playerType;
	
	public AbstractMove(PlayerType playerType) {
		this.playerType = playerType;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
}
