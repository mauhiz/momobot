package net.mauhiz.board.impl.go.data;

import net.mauhiz.board.model.data.Piece;

public class GoPiece implements Piece {
	private GoPlayerType playerType;
	private GoPieceType pieceType;
	

	public GoPiece(GoPlayerType playerType, GoPieceType pieceType) {
		this.playerType = playerType;
		this.pieceType = pieceType;
	}

	@Override
	public GoPlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public GoPieceType getPieceType() {
		return pieceType;
	}
	
	@Override
	public String toString() {
		return playerType.name().charAt(0) + "." + pieceType.getName();
	}

}
