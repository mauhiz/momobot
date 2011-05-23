package net.mauhiz.board.impl.chess.data;

import net.mauhiz.board.model.data.Piece;

public class ChessPiece implements Piece {
	private ChessPieceType pieceType;
	private ChessPlayerType playerType;

	public ChessPiece(ChessPlayerType playerType, ChessPieceType pieceType) {
		this.playerType = playerType;
		this.pieceType = pieceType;
	}

	public ChessPieceType getPieceType() {
		return pieceType;
	}

	public ChessPlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public String toString() {
		return playerType.name().charAt(0) + "." + pieceType.getName();
	}

}
