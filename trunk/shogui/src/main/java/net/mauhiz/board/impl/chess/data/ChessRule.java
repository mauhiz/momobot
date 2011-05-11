package net.mauhiz.board.impl.chess.data;

import static java.lang.Math.abs;

import java.util.List;

import net.mauhiz.board.impl.chess.Castle;
import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Rule;
import net.mauhiz.board.model.data.Square;

import org.apache.commons.lang.IllegalClassException;

public class ChessRule implements Rule {

	public boolean isFrontCorner(Square from, Square to, ChessPlayerType player) {
		return AbstractBoard.isCorner(from, to) && isForward(from, to, player);
	}

	/**
	 * @param from
	 * @param to
	 *            is different from 'from'
	 * @return
	 */
	public boolean canGo(ChessBoard b, Square from, Square to) {
		ChessPiece op = b.getPieceAt(from);

		if (b.isFriendlyPieceOn(op.getPlayerType(), to)) {
			return false;
		}

		switch (op.getPieceType()) {
			case PAWN:
				if (b.getPieceAt(to) == null) {
					return isPawnMove(from, to, op.getPlayerType());
				}
				return isFrontCorner(from, to, op.getPlayerType());
			case KNIGHT:
				return abs(AbstractBoard.getXmove(from, to)) == 1 && abs(AbstractBoard.getYmove(from, to)) == 2
						|| abs(AbstractBoard.getXmove(from, to)) == 2 && abs(AbstractBoard.getYmove(from, to)) == 1;
			case BISHOP:
				return AbstractBoard.isDiagonal(from, to) && !b.isObstruction(from, to);
			case ROOK:
				return AbstractBoard.isStraight(from, to) && !b.isObstruction(from, to);
			case KING:
				return abs(AbstractBoard.getXmove(from, to)) <= 1 && abs(AbstractBoard.getYmove(from, to)) <= 1;
			case QUEEN:
				return (AbstractBoard.isDiagonal(from, to) || AbstractBoard.isStraight(from, to))
						&& !b.isObstruction(from, to);
			default:
				throw new IllegalStateException();
		}
	}

	public boolean canPromote(ChessPiece op, Square to) {
		return ChessPieceType.PAWN.equals(op.getPieceType()) && isPromotionZone(op.getPlayerType(), to);
	}

	static boolean isPromotionZone(ChessPlayerType pl, Square here) {
		return ChessPlayerType.WHITE.equals(pl) ? here.getY() == 7 : here.getY() == 0;
	}

	public boolean canCastle(PlayerType playerType, boolean great, List<Move> history) {
		// TODO use move history (and status?)

		//		return playerType.x == 4 && abs(getXmove(playerType, b)) == 2 && getYmove(playerType, b) == 0
		//				&& playerType.y == (history == ChessPlayerType.WHITE ? 0 : 7) && !isObstruction(playerType, b)
		//				&& !isCheck(history);
		return false;
	}

	public boolean canEnPassant(ChessBoard board, Square from, Square to, ChessPlayerType player) {

		if (isFrontCorner(from, to, player)) {
			Square enPassant = SquareImpl.getInstance(to.getX(), from.getY());
			ChessPiece couic = board.getPieceAt(enPassant);
			if (couic == null || couic.getPieceType() != ChessPieceType.PAWN || couic.getPlayerType() == player) {
				return false;
			}

			// TODO use move history
			return true;
		}
		return false;
	}

	public boolean isPawnMove(Square from, Square to, ChessPlayerType player) {
		if (AbstractBoard.getXmove(from, to) == 0 && isForward(from, to, player)) {
			return from.getY() == (player == ChessPlayerType.WHITE ? 1 : 6) ? abs(AbstractBoard.getYmove(from, to)) <= 2
					: abs(AbstractBoard.getYmove(from, to)) == 1;
		}
		return false;
	}

	protected boolean isForward(Square from, Square to, ChessPlayerType player) {
		return from.getY() != to.getY() && player == ChessPlayerType.WHITE ^ from.getY() > to.getY();
	}

	public boolean isCheck(PlayerType player, ChessBoard board) {
		Square kingSquare = board.findKingSquare(player);
		if (kingSquare == null) {
			return false;
		}

		for (Square square : board.getSquares()) {
			ChessPiece attacker = board.getPieceAt(square);
			if (attacker == null || attacker.getPlayerType() == player) {
				continue;
			}
			if (canGo(board, square, kingSquare)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ChessBoard newBoard() {
		return new ChessBoard();
	}

	@Override
	public PlayerType[] getPlayerTypes() {
		return ChessPlayerType.values();
	}

	@Override
	public boolean isValid(Move move, Board board, List<Move> history) {
		if (!(board instanceof ChessBoard)) {
			throw new IllegalClassException(ChessBoard.class, board.getClass());
		}
		ChessBoard cb = (ChessBoard) board;
		if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
			return canGo(cb, nmove.getFrom(), nmove.getTo());
		} else if (move instanceof Castle) {
			Castle castle = (Castle) move;
			return canCastle(castle.getPlayerType(), castle.isGreat(), history);
		}
		return false;
	}

	@Override
	public void initPieces(Board board) {
		for (Square square : board.getSquares()) {
			int j = square.getY();
			ChessPlayerType pl = j <= 2 ? ChessPlayerType.WHITE : ChessPlayerType.BLACK;

			if (j == 0 || j == 7) {
				int i = square.getX();
				if (i == 0 || i == 7) {
					board.setPieceAt(square, new ChessPiece(pl, ChessPieceType.ROOK));
				} else if (i == 1 || i == 6) {
					board.setPieceAt(square, new ChessPiece(pl, ChessPieceType.KNIGHT));
				} else if (i == 2 || i == 5) {
					board.setPieceAt(square, new ChessPiece(pl, ChessPieceType.BISHOP));
				} else if (i == 3) {
					board.setPieceAt(square, new ChessPiece(pl, ChessPieceType.QUEEN));
				} else if (i == 4) {
					board.setPieceAt(square, new ChessPiece(pl, ChessPieceType.KING));
				}
			} else if (j == 1 || j == 6) {
				board.setPieceAt(square, new ChessPiece(pl, ChessPieceType.PAWN));
			}
		}
	}

	@Override
	public PlayerType getStartingPlayer() {
		return ChessPlayerType.WHITE;
	}
}
