package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.impl.common.data.AbstractGame;
import net.mauhiz.board.model.data.Move;

public class ShogiGame extends AbstractGame {

	public ShogiGame(ShogiRule rule) {
		super(rule);
	}

	@Override
	public ShogiPlayerType applyMove(Move move) {
		if (rule.isValid(move, this)) {
			ShogiBoard clone = getLastBoard().copy();
			clone.applyMove(move);
			boards.add(clone);
			moves.add(move);
			return getTurn();
		}

		return null;
	}

	@Override
	public ShogiBoard getLastBoard() {
		return (ShogiBoard) super.getLastBoard();
	}

	@Override
	public ShogiPlayerType getTurn() {
		return (ShogiPlayerType) super.getTurn();
	}
}
