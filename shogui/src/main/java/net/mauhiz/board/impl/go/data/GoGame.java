package net.mauhiz.board.impl.go.data;

import net.mauhiz.board.impl.common.data.AbstractGame;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public class GoGame extends AbstractGame {

	public GoGame(GoRule rule) {
		super(rule);
	}

	@Override
	public PlayerType applyMove(Move move) {
		if (rule.isValid(move, this)) {
			GoBoard clone = getLastBoard().copy();
			clone.applyMove(move);
			boards.add(clone);
			moves.add(move);
			return getTurn();
		}

		return null;
	}

	@Override
	public GoBoard getLastBoard() {
		return (GoBoard) super.getLastBoard();
	}
}
