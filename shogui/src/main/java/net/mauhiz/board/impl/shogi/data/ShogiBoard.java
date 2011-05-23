package net.mauhiz.board.impl.shogi.data;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.shogi.PromoteMove;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.PocketBoard;
import net.mauhiz.board.model.data.Square;

import org.apache.log4j.Logger;

public class ShogiBoard extends AbstractBoard implements PocketBoard {
	private static final Logger LOG = Logger.getLogger(ShogiBoard.class);
	public static final int SIZE = 9;
	protected final Map<ShogiPlayerType, List<ShogiPieceType>> pockets = new HashMap<ShogiPlayerType, List<ShogiPieceType>>(
			2);

	public ShogiBoard(ShogiRule rule) {
		super(rule);
		for (ShogiPlayerType spt : ShogiPlayerType.values()) {
			pockets.put(spt, new ArrayList<ShogiPieceType>());
		}
	}

	public void applyMove(Move move) {
		if (move instanceof Drop) {
			Drop drop = (Drop) move;
			ShogiPieceType pieceType = (ShogiPieceType) drop.getPieceType();
			ShogiPlayerType playerType = (ShogiPlayerType) drop.getPlayerType();
			if (pockets.get(playerType).remove(pieceType)) {
				setPieceAt(drop.getTo(), new ShogiPiece(playerType, pieceType));
			} else {
				LOG.warn("Player: " + playerType + " had no such piece: " + pieceType + " in his pocket");
			}
			LOG.debug("Drop applied: " + drop);

		} else if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
			ShogiPiece toMove = getPieceAt(nmove.getFrom());
			Piece oldPiece = piecesMap.remove(nmove.getFrom());

			if (oldPiece == null) {
				LOG.warn("Normal move: " + nmove + " has no piece at start square");
				return;
			}

			ShogiPiece capturedPiece = setPieceAt(nmove.getTo(), toMove);

			if (capturedPiece != null) {
				ShogiPieceType capturedPieceType = capturedPiece.getPieceType().reversePromotion();
				LOG.debug("Piece captured: " + capturedPiece);
				List<ShogiPieceType> pocket = pockets.get(nmove.getPlayerType());
				pocket.add(capturedPieceType);
				Collections.sort(pocket);
				LOG.trace("New pocket: " + pocket);
			}
			LOG.debug("Normal move applied: " + nmove);
		} else if (move instanceof PromoteMove) {
			PromoteMove pmove = (PromoteMove) move;
			NormalMove parentMove = pmove.getParentMove();
			applyMove(parentMove);
			ShogiPiece moved = (ShogiPiece) piecesMap.remove(parentMove.getTo());
			setPieceAt(parentMove.getTo(), new ShogiPiece((ShogiPlayerType) pmove.getPlayerType(), moved.getPieceType()
					.getPromotion()));
			LOG.debug("Promote move applied: " + pmove);
		}
	}

	public ShogiBoard copy() {
		ShogiBoard copy = new ShogiBoard(null);
		copy.piecesMap.putAll(piecesMap);
		for (Entry<ShogiPlayerType, List<ShogiPieceType>> ent : pockets.entrySet()) {
			copy.pockets.put(ent.getKey(), new ArrayList<ShogiPieceType>(ent.getValue()));
		}
		return copy;
	}

	public Square findKingSquare(ShogiPlayerType pl) {
		// locate the king
		for (Square square : getSquares()) {
			ShogiPiece op = getPieceAt(square);
			if (op == null) {
				continue;
			}
			if (op.getPlayerType() == pl && op.getPieceType() == ShogiPieceType.KING) {
				return square;
			}
		}
		LOG.warn("King not found for player: " + pl);
		return null;
	}

	public Collection<ShogiPiece> getAllPocketPieces() {
		List<ShogiPiece> pieces = new ArrayList<ShogiPiece>();
		for (Entry<ShogiPlayerType, List<ShogiPieceType>> ent : pockets.entrySet()) {
			ShogiPlayerType playerType = ent.getKey();

			for (ShogiPieceType pt : ent.getValue()) {
				pieces.add(new ShogiPiece(playerType, pt));
			}
		}

		return pieces;
	}

	@Override
	public ShogiPiece getPieceAt(Square square) {
		return (ShogiPiece) super.getPieceAt(square);
	}

	public List<ShogiPieceType> getPocket(PlayerType player) {
		return pockets.get(player);
	}

	public Dimension getSize() {
		return new Dimension(SIZE, SIZE);
	}

	@Override
	public ShogiPiece setPieceAt(Square square, Piece piece) {
		return (ShogiPiece) super.setPieceAt(square, piece);
	}
}
