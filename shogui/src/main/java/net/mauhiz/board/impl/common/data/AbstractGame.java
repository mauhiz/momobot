package net.mauhiz.board.impl.common.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Rule;

import org.apache.log4j.Logger;

/**
 * After move #i, we have board #i
 * @author mauhiz
 *
 */
public abstract class AbstractGame implements Game {
	private static final Logger LOG = Logger.getLogger(AbstractGame.class);

	protected final List<Board> boards = new ArrayList<>();
	protected final List<Move> moves = new ArrayList<>();
	protected final Rule rule;

	public AbstractGame(Rule rule) {
		this.rule = rule;
		boards.add(rule.newBoard());
		moves.add(new InitMove(rule.getStartingPlayer()));
	}

	public PlayerType applyMove(Move move) {
		if (move instanceof InitMove) {
			return getRule().getStartingPlayer();
		}
		if (move.getPlayerType() != getTurn()) {
			LOG.warn("Wrong turn: " + move);
			return null;
		}
		Board lastBoard = getLastBoard();
		if (rule.preCheck(move, lastBoard, this)) {
			LOG.trace("Applying move: " + move);
			LOG.trace("Cloning board: " + lastBoard);
			Board clone = lastBoard.copy();
			clone.applyMove(move);
			if (rule.postCheck(move, clone, this)) {
				boards.add(clone);
				moves.add(move);
				return getTurn();
			}
		}

		LOG.warn("Move rejected: " + move);
		return null;
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
