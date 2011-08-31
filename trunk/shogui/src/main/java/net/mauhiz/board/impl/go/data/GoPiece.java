package net.mauhiz.board.impl.go.data;

import net.mauhiz.board.impl.common.data.DefaultPiece;

public class GoPiece extends DefaultPiece {

	public GoPiece(GoPlayerType playerType, GoPieceType pieceType) {
		super(playerType, pieceType);
	}

	@Override
	public GoPieceType getPieceType() {
		return (GoPieceType) super.getPieceType();
	}

	@Override
	public GoPlayerType getPlayerType() {
		return (GoPlayerType) super.getPlayerType();
	}
}
