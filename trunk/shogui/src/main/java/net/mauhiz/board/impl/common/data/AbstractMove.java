package net.mauhiz.board.impl.common.data;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public class AbstractMove implements Move {
	private PlayerType player;
	
	public AbstractMove(PlayerType playerType) {
		this.player = playerType;
	}
	
	public PlayerType getPlayerType() {
		return player;
	}
}
