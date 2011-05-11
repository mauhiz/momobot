package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.model.data.Piece;

public class ShogiPiece implements Piece {
	private ShogiPlayerType playerType;
	private ShogiPieceType pieceType;
	

	public ShogiPiece(ShogiPlayerType playerType, ShogiPieceType pieceType) {
		this.playerType = playerType;
		this.pieceType = pieceType;
	}

	@Override
	public ShogiPlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public ShogiPieceType getPieceType() {
		return pieceType;
	}
	
	@Override
	public String toString() {
		return playerType.name().charAt(0) + "." + pieceType.getName();
	}

}
