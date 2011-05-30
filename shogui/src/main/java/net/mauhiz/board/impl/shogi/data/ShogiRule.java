package net.mauhiz.board.impl.shogi.data;

import static java.lang.Math.abs;
import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.common.data.AbstractPocketRule;
import net.mauhiz.board.impl.common.data.NormalMoveImpl;
import net.mauhiz.board.impl.shogi.PromoteMove;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

import org.apache.log4j.Logger;

public class ShogiRule extends AbstractPocketRule {

	private static final Logger LOG = Logger.getLogger(ShogiRule.class);

	private static boolean isPromotionZone(ShogiPlayerType player, Square square) {
		return player == ShogiPlayerType.SENTE ? square.getY() >= 6 : square.getY() <= 2;
	}

	/**
	 * @param from
	 * @param to
	 *            is different from 'from'
	 * @return
	 */
	private boolean canGo(Board board, Square from, Square to) {
		Piece piece = board.getPieceAt(from);
		ShogiPlayerType playerType = (ShogiPlayerType) piece.getPlayerType();

		if (board.isFriendlyPieceOn(playerType, to)) {
			return false;
		}

		ShogiPieceType pieceType = (ShogiPieceType) piece.getPieceType();

		switch (pieceType) {
		case PAWN:
			return isPawnMove(from, to, playerType);
		case LANCE:
			return !board.isObstruction(from, to) && AbstractBoard.getXmove(from, to) == 0
					&& AbstractBoard.getYmove(from, to) * (playerType == ShogiPlayerType.SENTE ? 1 : -1) > 0;
		case SILVER:
			return AbstractBoard.isCorner(from, to) || isPawnMove(from, to, playerType);
		case KNIGHT:
			return abs(AbstractBoard.getXmove(from, to)) == 1
					&& AbstractBoard.getYmove(from, to) == (playerType == ShogiPlayerType.SENTE ? 2 : -2);

		case BISHOP:
			return AbstractBoard.isDiagonal(from, to) && !board.isObstruction(from, to);
		case ROOK:
			return AbstractBoard.isStraight(from, to) && !board.isObstruction(from, to);
		case KING:
			return abs(AbstractBoard.getXmove(from, to)) <= 1 && abs(AbstractBoard.getYmove(from, to)) <= 1;
		case TOKIN:
		case GOLD:
		case PROMOTED_LANCE:
		case PROMOTED_SILVER:
		case PROMOTED_KNIGHT:
			return isGoldMove(from, to, playerType);
		case HORSE:
			return AbstractBoard.isDiagonal(from, to) && !board.isObstruction(from, to)
					|| AbstractBoard.isCross(from, to);
		case DRAGON:
			return AbstractBoard.isStraight(from, to) && !board.isObstruction(from, to)
					|| AbstractBoard.isCorner(from, to);
		default:
			throw new IllegalStateException();
		}
	}

	public boolean canPromote(Board board, Square from, Square to) {
		Piece piece = board.getPieceAt(from);
		if (piece == null) {
			return false;
		}
		ShogiPieceType pieceType = (ShogiPieceType) piece.getPieceType();

		if (!pieceType.canPromote()) {
			return false;
		}

		ShogiPlayerType playerType = (ShogiPlayerType) piece.getPlayerType();
		return isPromotionZone(playerType, from) || isPromotionZone(playerType, to);
	}

	public ShogiPlayerType[] getPlayerTypes() {
		return ShogiPlayerType.values();
	}

	public ShogiPlayerType getStartingPlayer() {
		return ShogiPlayerType.SENTE;
	}

	public void initPieces(Board board) {
		for (Square square : board.getSquares()) {
			int j = square.getY();
			int i = square.getX();
			ShogiPlayerType pl = j <= 2 ? ShogiPlayerType.SENTE : ShogiPlayerType.GOTE;

			if (j == 0 || j == 8) {
				if (i == 0 || i == 8) {
					board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.LANCE));
				} else if (i == 1 || i == 7) {
					board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.KNIGHT));
				} else if (i == 2 || i == 6) {
					board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.SILVER));
				} else if (i == 3 || i == 5) {
					board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.GOLD));
				} else {
					board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.KING));
				}
			} else if (j == 1 && i == 1 || j == 7 && i == 7) {
				board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.BISHOP));
			} else if (j == 1 && i == 7 || j == 7 && i == 1) {
				board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.ROOK));
			} else if (j == 2 || j == 6) {
				board.setPieceAt(square, new ShogiPiece(pl, ShogiPieceType.PAWN));
			}
		}
		LOG.info("Pieces init complete");
	}

	public boolean isCheck(PlayerType player, Game game, Board board) {
		Square kingSquare = board.findSquare(player, ShogiPieceType.KING);
		if (kingSquare == null) {
			LOG.warn("Player: " + player + " has no king!");
			return false;
		}

		for (Square square : board.getSquares()) {
			Piece attacker = board.getPieceAt(square);
			if (attacker == null || attacker.getPlayerType() == player) {
				continue;
			}

			NormalMove wannabe = new NormalMoveImpl(attacker.getPlayerType(), square, kingSquare);

			if (preCheck(wannabe, board, game)) {
				LOG.debug("Ote: " + wannabe);
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean isForward(Square from, Square to, PlayerType player) {
		return from.getY() != to.getY() && player == ShogiPlayerType.SENTE ^ from.getY() > to.getY();
	}

	private boolean isGoldMove(Square from, Square to, ShogiPlayerType player) {
		return AbstractBoard.isCross(from, to) || isFrontCorner(from, to, player);
	}

	private boolean isPawnMove(Square from, Square to, ShogiPlayerType player) {
		return AbstractBoard.getXmove(from, to) == 0 && abs(AbstractBoard.getYmove(from, to)) == 1
				&& isForward(from, to, player);
	}

	public ShogiBoard newBoard() {
		return new ShogiBoard(this);
	}

	public boolean postCheck(Move move, Board newBoard, Game game) {
		// rule : don't be in check after play
		if (isCheck(move.getPlayerType(), game, newBoard)) {
			return false;
		}

		return true;
	}

	public boolean preCheck(Move move, Board oldBoard, Game game) {
		if (move instanceof Drop) {
			Drop drop = (Drop) move;
			Square to = drop.getTo();
			if (oldBoard.getPieceAt(to) == null) {
				ShogiPieceType spt = (ShogiPieceType) drop.getPieceType();
				ShogiPlayerType player = (ShogiPlayerType) move.getPlayerType();
				switch (spt) {
				case ROOK:
				case SILVER:
				case BISHOP:
					return true;
				case KNIGHT:
					return !isPromotionZone(player, to);
				case PAWN:
					int x = to.getX();
					if (((ShogiBoard) oldBoard).hasPawnOnColumn(player, x)) {
						LOG.debug("Player: " + player + " already had a pawn on column: " + x);
						return false;
					}
					//$FALL-THROUGH$
				case LANCE:
					return player == ShogiPlayerType.SENTE ? to.getY() != 7 : to.getY() != 0;
				default:
					return false;
				}
			}

		} else if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
			Square from = nmove.getFrom();
			Piece toMove = oldBoard.getPieceAt(from);

			if (toMove == null || toMove.getPlayerType() != move.getPlayerType()) {
				return false;
			}

			return canGo(oldBoard, from, nmove.getTo());
		} else if (move instanceof PromoteMove) {
			PromoteMove promote = (PromoteMove) move;
			return canPromote(oldBoard, promote.getParentMove().getFrom(), promote.getParentMove().getTo());
		}

		return false;
	}
}
