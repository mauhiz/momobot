package net.mauhiz.board.impl.checkers.data;

import net.mauhiz.board.impl.common.data.AbstractPiece;

public class CheckersPiece extends AbstractPiece {

	public CheckersPiece(CheckersPlayerType playerType, CheckersPieceType pieceType) {
		super(playerType, pieceType);
	}

	@Override
	public CheckersPieceType getPieceType() {
		return (CheckersPieceType) super.getPieceType();
	}

	@Override
	public CheckersPlayerType getPlayerType() {
		return (CheckersPlayerType) super.getPlayerType();
	}
}
