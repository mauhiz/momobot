package net.mauhiz.board.impl.chess.data;

import java.awt.Dimension;

import net.mauhiz.board.impl.chess.Castle;
import net.mauhiz.board.impl.chess.EnPassant;
import net.mauhiz.board.impl.chess.PromoteMove;
import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.impl.common.data.SquareView;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Rule;
import net.mauhiz.board.model.data.Square;

public class ChessBoard extends AbstractBoard {
	public static enum Status {
		CHECK(false), DRAW(true), MATE(true);
		private final boolean end;

		private Status(boolean end) {
			this.end = end;
		}

		public boolean isEnd() {
			return end;
		}
	}

	public static final int SIZE = 8;

	public ChessBoard(Rule rule) {
		super(rule);
	}

	public void applyMove(Move move) {
		if (move instanceof EnPassant) {
			EnPassant passant = (EnPassant) move;
			Square from = passant.getFrom();
			Square to = passant.getTo();
			Square enPassant = SquareImpl.getInstance(to.getX(), from.getY());
			piecesMap.remove(enPassant);
		}
		if (move instanceof NormalMove) {
			Square from = ((NormalMove) move).getFrom();
			Square to = ((NormalMove) move).getTo();
			piecesMap.put(to, piecesMap.remove(from));

		} else if (move instanceof Castle) {
			Castle castle = (Castle) move;
			int row = move.getPlayerType() == ChessPlayerType.WHITE ? 0 : 7;
			Square kingFrom = SquareImpl.getInstance(4, row);
			Square kingTo = SquareImpl.getInstance(castle.isGreat() ? 2 : 6, row);
			piecesMap.put(kingTo, piecesMap.remove(kingFrom));

			// move the rook too
			Square rookFrom = SquareImpl.getInstance(castle.isGreat() ? 0 : 7, row);
			Square rookTo = SquareImpl.getInstance(castle.isGreat() ? 3 : 5, row);
			piecesMap.put(rookTo, piecesMap.remove(rookFrom));
		} else if (move instanceof PromoteMove) {
			PromoteMove pmove = (PromoteMove) move;
			NormalMove parentMove = pmove.getParentMove();
			applyMove(parentMove);
			piecesMap.put(parentMove.getTo(),
					new ChessPiece((ChessPlayerType) pmove.getPlayerType(), pmove.getPromotion()));
		}
	}

	public ChessBoard copy() {
		ChessBoard copy = new ChessBoard(null);
		copy.piecesMap.putAll(piecesMap);
		return copy;
	}

	public Square findKingSquare(PlayerType pl) {
		// locate the king
		for (Square square : new SquareView(getSize())) {
			ChessPiece op = getPieceAt(square);
			if (op == null) {
				continue;
			}
			if (op.getPlayerType() == pl && op.getPieceType() == ChessPieceType.KING) {
				return square;
			}
		}

		return null;
	}

	@Override
	public ChessPiece getPieceAt(Square square) {
		return (ChessPiece) super.getPieceAt(square);
	}

	public Dimension getSize() {
		return new Dimension(SIZE, SIZE);
	}
}
