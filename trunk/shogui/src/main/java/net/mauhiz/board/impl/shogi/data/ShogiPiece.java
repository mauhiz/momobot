package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.model.data.Piece;

public class ShogiPiece implements Piece {
	private ShogiPieceType pieceType;
	private ShogiPlayerType playerType;

	public ShogiPiece(ShogiPlayerType playerType, ShogiPieceType pieceType) {
		this.playerType = playerType;
		this.pieceType = pieceType;
	}

	public ShogiPieceType getPieceType() {
		return pieceType;
	}

	public ShogiPlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public String toString() {
		return playerType.name().charAt(0) + "." + pieceType.getName();
	}

}
