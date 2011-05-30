package net.mauhiz.board.impl.chess.data;

import net.mauhiz.board.impl.common.data.AbstractPiece;

public class ChessPiece extends AbstractPiece {

	public ChessPiece(ChessPlayerType playerType, ChessPieceType pieceType) {
		super(playerType, pieceType);
	}

	@Override
	public ChessPieceType getPieceType() {
		return (ChessPieceType) super.getPieceType();
	}

	@Override
	public ChessPlayerType getPlayerType() {
		return (ChessPlayerType) super.getPlayerType();
	}
}
