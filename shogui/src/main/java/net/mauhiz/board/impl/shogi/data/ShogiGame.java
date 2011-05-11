package net.mauhiz.board.impl.shogi.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public class ShogiGame implements Game {
	private PlayerType turn;
	private final ShogiRule rule;
	private final ShogiBoard board;
	private final List<Move> history = new ArrayList<Move>();
	
	
	public ShogiGame(ShogiRule rule) {
		this.rule = rule;
		this.board = rule.newBoard();
		turn = rule.getStartingPlayer();
		rule.initPieces(board);
	}

	@Override
	public ShogiBoard getBoard() {
		return board;
	}

	@Override
	public List<Move> getMoves() {
		return history;
	}

	@Override
	public ShogiRule getRule() {
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
