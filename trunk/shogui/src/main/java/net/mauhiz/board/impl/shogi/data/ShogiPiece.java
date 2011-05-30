package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.impl.common.data.AbstractPiece;

public class ShogiPiece extends AbstractPiece {

	public ShogiPiece(ShogiPlayerType playerType, ShogiPieceType pieceType) {
		super(playerType, pieceType);
	}

	@Override
	public ShogiPieceType getPieceType() {
		return (ShogiPieceType) super.getPieceType();
	}

	@Override
	public ShogiPlayerType getPlayerType() {
		return (ShogiPlayerType) super.getPlayerType();
	}
}
