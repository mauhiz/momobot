package net.mauhiz.board.impl.go.data;

import net.mauhiz.board.model.data.Piece;

public class GoPiece implements Piece {
	private GoPieceType pieceType;
	private GoPlayerType playerType;

	public GoPiece(GoPlayerType playerType, GoPieceType pieceType) {
		this.playerType = playerType;
		this.pieceType = pieceType;
	}

	public GoPieceType getPieceType() {
		return pieceType;
	}

	public GoPlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public String toString() {
		return playerType.name().charAt(0) + "." + pieceType.getName();
	}

}
