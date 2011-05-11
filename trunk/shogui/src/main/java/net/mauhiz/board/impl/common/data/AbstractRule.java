package net.mauhiz.board.impl.common.data;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Rule;
import net.mauhiz.board.model.data.Square;

public abstract class AbstractRule implements Rule {

	@Override
	public Move generateMove(Square from, Square to, Game game) {
		if (to != from) {
			NormalMove move = new NormalMoveImpl(game.getTurn(), from, to);
			if (isValid(move, game)) {
				return move;
			}
		}

		return null;
	}

	protected abstract boolean isForward(Square from, Square to, PlayerType player);

	protected boolean isFrontCorner(Square from, Square to, PlayerType player) {
		return AbstractBoard.isCorner(from, to) && isForward(from, to, player);
	}
}
