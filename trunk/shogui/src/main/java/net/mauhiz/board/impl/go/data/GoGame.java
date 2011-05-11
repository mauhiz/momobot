package net.mauhiz.board.impl.go.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public class GoGame implements Game {
	private PlayerType turn;
	private final GoRule rule;
	private final GoBoard board;
	private final List<Move> history = new ArrayList<Move>();
	
	
	public GoGame(GoRule rule) {
		this.rule = rule;
		this.board = rule.newBoard();
		turn = rule.getStartingPlayer();
		rule.initPieces(board);
	}

	@Override
	public GoBoard getBoard() {
		return board;
	}

	@Override
	public List<Move> getMoves() {
		return history;
	}

	@Override
	public GoRule getRule() {
		return rule;
	}

	@Override
	public PlayerType applyMove(Move move) {
		if (rule.isValid(move, this)) {
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
