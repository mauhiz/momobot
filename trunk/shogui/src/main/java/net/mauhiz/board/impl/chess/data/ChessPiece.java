package net.mauhiz.board.impl.chess.data;

import net.mauhiz.board.model.data.Piece;

public class ChessPiece implements Piece {
	private ChessPlayerType playerType;
	private ChessPieceType pieceType;
	

	public ChessPiece(ChessPlayerType playerType, ChessPieceType pieceType) {
		this.playerType = playerType;
		this.pieceType = pieceType;
	}

	@Override
	public ChessPlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public ChessPieceType getPieceType() {
		return pieceType;
	}
	
	@Override
	public String toString() {
		return playerType.name().charAt(0) + "." + pieceType.getName();
	}

}
