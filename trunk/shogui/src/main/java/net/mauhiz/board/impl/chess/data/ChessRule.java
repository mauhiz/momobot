package net.mauhiz.board.impl.chess.data;

import static java.lang.Math.abs;
import net.mauhiz.board.impl.chess.Castle;
import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.common.data.AbstractRule;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

import org.apache.commons.lang.IllegalClassException;

public class ChessRule extends AbstractRule {

	static boolean isPromotionZone(ChessPlayerType pl, Square here) {
		return ChessPlayerType.WHITE.equals(pl) ? here.getY() == 7 : here.getY() == 0;
	}

	private boolean canEnPassant(ChessBoard board, Square from, Square to, ChessPlayerType player) {

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

	/**
	 * @param from
	 * @param to
	 *            is different from 'from'
	 * @return
	 */
	private boolean canGo(ChessBoard b, Square from, Square to) {
		ChessPiece op = b.getPieceAt(from);

		if (b.isFriendlyPieceOn(op.getPlayerType(), to)) {
			return false;
		}

		switch (op.getPieceType()) {
		case PAWN:
			if (b.getPieceAt(to) == null) {
				return isPawnMove(from, to, op.getPlayerType()) || canEnPassant(b, from, to, op.getPlayerType());
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
		return ChessPieceType.PAWN == op.getPieceType() && isPromotionZone(op.getPlayerType(), to);
	}

	private Castle generateCastle(Square from, Square to, ChessGame game) {
		Piece wannabeKing = game.getLastBoard().getPieceAt(from);
		if (wannabeKing == null || wannabeKing.getPieceType() != ChessPieceType.KING
				|| wannabeKing.getPlayerType() != game.getTurn()) {
			return null;
		}
		if (abs(AbstractBoard.getXmove(from, to)) == 2 && AbstractBoard.getYmove(from, to) == 0) {
			boolean great = AbstractBoard.getXmove(from, to) < 0;
			Square rookStart = SquareImpl.getInstance(great ? 0 : 7, from.getY());
			if (game.getLastBoard().isObstruction(from, rookStart)) {
				return null;
			}
			if (game.kingHasMoved(game.getTurn()) || game.rookHasMoved(game.getTurn(), rookStart)) {
				return null;
			}

			for (int x = from.getX(); great ? x >= to.getX() : x <= to.getX(); x += great ? -1 : 1) {
				if (isCheck(game.getTurn(), game.getLastBoard(), SquareImpl.getInstance(x, from.getY()))) {
					return null;
				}
			}

			return new Castle(game.getTurn(), great);
		}
		return null;
	}

	@Override
	public Move generateMove(Square from, Square to, Game game) {
		Move move = super.generateMove(from, to, game);
		if (move == null) {
			// try to castle
			return generateCastle(from, to, (ChessGame) game);

		}
		return move;
	}

	@Override
	public ChessPlayerType[] getPlayerTypes() {
		return ChessPlayerType.values();
	}

	@Override
	public ChessPlayerType getStartingPlayer() {
		return ChessPlayerType.WHITE;
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

	public boolean isCheck(PlayerType player, ChessBoard board) {
		Square kingSquare = board.findKingSquare(player);
		if (kingSquare == null) {
			return false;
		}
		return isCheck(player, board, kingSquare);
	}

	private boolean isCheck(PlayerType player, ChessBoard board, Square kingSquare) {

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
	protected boolean isForward(Square from, Square to, PlayerType player) {
		return from.getY() != to.getY() && player == ChessPlayerType.WHITE ^ from.getY() > to.getY();
	}

	public boolean isPawnMove(Square from, Square to, ChessPlayerType player) {
		if (AbstractBoard.getXmove(from, to) == 0 && isForward(from, to, player)) {
			return from.getY() == (player == ChessPlayerType.WHITE ? 1 : 6) ? abs(AbstractBoard.getYmove(from, to)) <= 2
					: abs(AbstractBoard.getYmove(from, to)) == 1;
		}
		return false;
	}

	@Override
	public boolean isValid(Move move, Game game) {
		if (!(game instanceof ChessGame)) {
			throw new IllegalClassException(ChessGame.class, game.getClass());
		}
		if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
			return canGo((ChessBoard) game.getLastBoard(), nmove.getFrom(), nmove.getTo());
		} else if (move instanceof Castle) {
			return true;
		}
		return false;
	}

	@Override
	public ChessBoard newBoard() {
		return new ChessBoard(this);
	}
}
