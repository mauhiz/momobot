package net.mauhiz.board.impl.chess.data;

import net.mauhiz.board.impl.chess.Castle;
import net.mauhiz.board.impl.common.data.AbstractGame;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public class ChessGame extends AbstractGame {

	public ChessGame(ChessRule rule) {
		super(rule);
	}

	public ChessPlayerType applyMove(Move move) {
		if (rule.isValid(move, this)) {
			ChessBoard clone = getLastBoard().copy();
			clone.applyMove(move);
			if (getRule().isCheck(getTurn(), clone)) {
				return null;
			}
			boards.add(clone);
			moves.add(move);
			return getTurn();
		}

		return null;
	}

	@Override
	public ChessBoard getLastBoard() {
		return (ChessBoard) super.getLastBoard();
	}

	@Override
	public ChessRule getRule() {
		return (ChessRule) super.getRule();
	}

	@Override
	public ChessPlayerType getTurn() {
		return (ChessPlayerType) super.getTurn();
	}

	public boolean kingHasMoved(PlayerType kingOwner) {
		for (Move move : moves) {
			if (move.getPlayerType() == kingOwner) {
				if (move instanceof Castle) {
					return true;
				} else if (move instanceof NormalMove) {
					NormalMove nmove = (NormalMove) move;
					if (nmove.getFrom().getX() == 4
							&& nmove.getFrom().getY() == (kingOwner == ChessPlayerType.WHITE ? 0 : 7)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean rookHasMoved(PlayerType rookOwner, Square rookStart) {
		for (Move move : moves) {
			if (move.getPlayerType() == rookOwner && move instanceof NormalMove) {
				NormalMove nmove = (NormalMove) move;
				if (nmove.getFrom() == rookStart) {
					return true;
				}
			}
		}
		return false;
	}

}
