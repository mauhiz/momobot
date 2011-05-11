package net.mauhiz.board.impl.chess.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public class ChessGame implements Game {
	private PlayerType turn;
	private final ChessRule rule;
	private final ChessBoard board;
	private final List<Move> history = new ArrayList<Move>();

	public ChessGame(ChessRule rule) {
		this.rule = rule;
		this.board = rule.newBoard();
		turn = rule.getStartingPlayer();
		rule.initPieces(board);
	}

	@Override
	public ChessBoard getBoard() {
		return board;
	}

	@Override
	public List<Move> getMoves() {
		return history;
	}

	@Override
	public ChessRule getRule() {
		return rule;
	}

	@Override
	public PlayerType applyMove(Move move) {
		if (rule.isValid(move, board, history)) {
			history.add(move);
			turn = move.getPlayerType().other();
			board.applyMove(move);
			return turn;
		}

		return null;
	}

	@Override
	public PlayerType getTurn() {
		return turn;
	}

}
