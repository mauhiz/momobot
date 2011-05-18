package net.mauhiz.board.impl.go.data;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.PocketBoard;
import net.mauhiz.board.model.data.Square;

public class GoBoard extends AbstractBoard implements PocketBoard {
	protected final Map<GoPlayerType, List<GoPieceType>> captures = new HashMap<GoPlayerType, List<GoPieceType>>(2);

	public GoBoard(GoRule rule) {
		super(rule);
		for (GoPlayerType spt : GoPlayerType.values()) {
			captures.put(spt, new ArrayList<GoPieceType>());
		}
	}

	public void applyMove(Move move) {
		if (move instanceof Drop) {
			Drop drop = (Drop) move;
			GoPieceType pieceType = (GoPieceType) drop.getPieceType();
			GoPlayerType playerType = (GoPlayerType) drop.getPlayerType();
			setPieceAt(drop.getTo(), new GoPiece(playerType, pieceType));
		}
	}

	@Override
	public GoBoard copy() {
		GoBoard copy = new GoBoard(null);
		copy.piecesMap.putAll(piecesMap);
		for (Entry<GoPlayerType, List<GoPieceType>> ent : captures.entrySet()) {
			copy.captures.put(ent.getKey(), new ArrayList<GoPieceType>(ent.getValue()));
		}
		return copy;
	}

	public Collection<GoPiece> getAllPocketPieces() {
		List<GoPiece> pieces = new ArrayList<GoPiece>();
		for (GoPlayerType playerType : GoPlayerType.values()) {
			pieces.add(new GoPiece(playerType, GoPieceType.STONE));
		}

		return pieces;
	}

	@Override
	public GoPiece getPieceAt(Square square) {
		return (GoPiece) super.getPieceAt(square);
	}

	@Override
	public List<GoPieceType> getPocket(PlayerType player) {
		return Collections.singletonList(GoPieceType.STONE);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(19, 19);
	}

	@Override
	public GoPiece setPieceAt(Square square, Piece piece) {
		return (GoPiece) super.setPieceAt(square, piece);
	}
}
