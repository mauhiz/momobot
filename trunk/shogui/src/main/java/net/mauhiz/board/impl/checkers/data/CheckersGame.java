package net.mauhiz.board.impl.checkers.data;

import net.mauhiz.board.impl.checkers.PromoteMove;
import net.mauhiz.board.impl.common.data.AbstractGame;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;

public class CheckersGame extends AbstractGame {

	public CheckersGame(CheckersRule rule) {
		super(rule);
	}

	@Override
	public CheckersPlayerType applyMove(Move move) {
		if (rule.isValid(move, this)) {
			Move realMove = move;

			if (move instanceof NormalMove) {
				NormalMove nmove = (NormalMove) move;
				if (getRule().canPromote(nmove)) {
					realMove = new PromoteMove(nmove);
				}
			}
			moves.add(realMove);
			Board newState = getLastBoard().copy();
			newState.applyMove(realMove);
			boards.add(newState);
			return getTurn();
		}

		return null;
	}

	@Override
	public CheckersBoard getLastBoard() {
		return (CheckersBoard) super.getLastBoard();
	}

	@Override
	public CheckersRule getRule() {
		return (CheckersRule) super.getRule();
	}

	@Override
	public CheckersPlayerType getTurn() {
		// TODO can capture again with same piece if capture
		return (CheckersPlayerType) super.getTurn();
	}

}
