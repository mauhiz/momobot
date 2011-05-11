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

public class ShogiBoard extends AbstractBoard implements PocketBoard {
	protected final Map<ShogiPlayerType, List<ShogiPieceType>> pockets = new HashMap<ShogiPlayerType, List<ShogiPieceType>>(2);

	public ShogiBoard() {
		super();
		for (ShogiPlayerType spt : ShogiPlayerType.values()) {
			pockets.put(spt, new ArrayList<ShogiPieceType>());
		}
	}

	@Override
	public List<ShogiPieceType> getPocket(PlayerType player) {
		return pockets.get(player);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(9, 9);
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

		return null;
	}
	
	@Override
	public ShogiPiece getPieceAt(Square square) {
		return (ShogiPiece) super.getPieceAt(square);
	}
	
	@Override
	public ShogiPiece setPieceAt(Square square, Piece piece) {
		return (ShogiPiece) super.setPieceAt(square, piece);
	}

	public void applyMove(Move move) {
		if (move instanceof Drop) {
			Drop drop = (Drop) move;
			ShogiPieceType pieceType = (ShogiPieceType) drop.getPieceType();
			ShogiPlayerType playerType = (ShogiPlayerType) drop.getPlayerType();
			pockets.get(playerType).remove(pieceType);
			setPieceAt(drop.getTo(), new ShogiPiece(playerType, pieceType));

		} else if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
			ShogiPiece toMove = getPieceAt(nmove.getFrom());
			piecesMap.remove(nmove.getFrom());

			ShogiPiece capturedPiece = setPieceAt(nmove.getTo(), toMove);

			if (capturedPiece != null) {
				List<ShogiPieceType> pocket = pockets.get(nmove.getPlayerType());
				pocket.add(capturedPiece.getPieceType().reversePromotion());
				Collections.sort(pocket);
			}
		} else if (move instanceof PromoteMove) {
			PromoteMove pmove = (PromoteMove) move;
			NormalMove parentMove = pmove.getParentMove();
			applyMove(parentMove);
			ShogiPiece moved = (ShogiPiece) piecesMap.remove(parentMove.getTo());
			setPieceAt(parentMove.getTo(), new ShogiPiece((ShogiPlayerType) pmove.getPlayerType(), moved
					.getPieceType().getPromotion()));
		}
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
}
