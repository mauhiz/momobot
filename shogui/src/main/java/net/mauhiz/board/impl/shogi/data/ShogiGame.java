package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.impl.common.data.AbstractGame;
import net.mauhiz.board.impl.common.data.InitMove;
import net.mauhiz.board.model.data.Move;

import org.apache.log4j.Logger;

public class ShogiGame extends AbstractGame {

	private static final Logger LOG = Logger.getLogger(ShogiGame.class);

	public ShogiGame(ShogiRule rule) {
		super(rule);
	}

	public ShogiPlayerType applyMove(Move move) {
		if (move instanceof InitMove) {
			return getRule().getStartingPlayer();
		}
		if (rule.isValid(move, this)) {
			LOG.trace("Applying move: " + move);
			LOG.trace("Cloning board: " + getLastBoard());
			ShogiBoard clone = getLastBoard().copy();
			clone.applyMove(move);
			boards.add(clone);
			moves.add(move);
			return getTurn();
		}

		LOG.warn("Move rejected: " + move);
		return null;
	}

	@Override
	public ShogiBoard getLastBoard() {
		return (ShogiBoard) super.getLastBoard();
	}

	@Override
	public ShogiRule getRule() {
		return (ShogiRule) super.getRule();
	}

	@Override
	public ShogiPlayerType getTurn() {
		return (ShogiPlayerType) super.getTurn();
	}
}
