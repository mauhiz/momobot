package net.mauhiz.board.impl.go.data;

import net.mauhiz.board.impl.common.data.AbstractPocketRule;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public class GoRule extends AbstractPocketRule {

	private boolean canDrop(Board board, Square to) {
		return board.getPieceAt(to) == null;
	}

	public GoPlayerType[] getPlayerTypes() {
		return GoPlayerType.values();
	}

	public GoPlayerType getStartingPlayer() {
		return GoPlayerType.BLACK;
	}

	public void initPieces(Board board) {
		// starts with empty board
	}

	@Override
	protected boolean isForward(Square from, Square to, PlayerType player) {
		return false;
	}

	public boolean isValid(Move move, Game game) {
		if (!(game instanceof GoGame)) {
			return false;
		}
		if (move.getPlayerType() != game.getTurn()) {
			return false;
		}
		GoGame sg = (GoGame) game;
		GoBoard board = sg.getLastBoard();
		if (move instanceof Drop) {
			return canDrop(board, ((Drop) move).getTo());
		}
		return false;
	}

	public GoBoard newBoard() {
		return new GoBoard(this);
	}
}
