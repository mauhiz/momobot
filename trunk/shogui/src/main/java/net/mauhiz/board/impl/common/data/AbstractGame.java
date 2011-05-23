package net.mauhiz.board.impl.common.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Rule;

/**
 * After move #i, we have board #i
 * @author mauhiz
 *
 */
public abstract class AbstractGame implements Game {
	protected final List<Board> boards = new ArrayList<Board>();
	protected final List<Move> moves = new ArrayList<Move>();
	protected final Rule rule;

	public AbstractGame(Rule rule) {
		this.rule = rule;
		boards.add(rule.newBoard());
		moves.add(new InitMove(rule.getStartingPlayer()));
	}

	public Board getBoard(int i) {
		return boards.get(i);
	}

	public Board getLastBoard() {
		return getBoard(boards.size() - 1);
	}

	public Move getLastMove() {
		return getMove(moves.size() - 1);
	}

	public Move getMove(int i) {
		return moves.get(i);
	}

	public Iterable<Move> getMoves() {
		return moves;
	}

	public Rule getRule() {
		return rule;
	}

	public PlayerType getTurn() {
		Move move = getLastMove();
		return move instanceof InitMove ? rule.getStartingPlayer() : move.getPlayerType().other();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + moves;
	}
}
