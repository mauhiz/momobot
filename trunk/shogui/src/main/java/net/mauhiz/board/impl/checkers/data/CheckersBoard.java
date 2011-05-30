package net.mauhiz.board.impl.checkers.data;

import java.awt.Dimension;

import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Square;

/**
 * @author mauhiz
 */
public class CheckersBoard extends AbstractBoard {
	public static final int SIZE = 10;

	static Square getSkippedSquare(Square from, Square to) {
		if (isCornerSkip(from, to)) {
			return SquareImpl.getInstance((from.getX() + to.getX()) / 2, (from.getY() + to.getY()) / 2);
		}
		return null;
	}

	public CheckersBoard(CheckersRule rule) {
		super(rule);
	}

	public void applyMove(Move move) {
		if (move instanceof NormalMove) {
			Square from = ((NormalMove) move).getFrom();
			Square to = ((NormalMove) move).getTo();
			movePiece(from, to);

			// capture
			setPieceAt(getSkippedSquare(from, to), null);
		}
	}

	public CheckersBoard copy() {
		CheckersBoard copy = new CheckersBoard(null);
		super.copyInto(copy);
		return copy;
	}

	@Override
	public CheckersPiece getPieceAt(Square square) {
		return (CheckersPiece) super.getPieceAt(square);
	}

	public Dimension getSize() {
		return new Dimension(SIZE, SIZE);
	}
}
