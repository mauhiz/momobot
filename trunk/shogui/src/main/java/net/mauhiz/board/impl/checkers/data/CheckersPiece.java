package net.mauhiz.board.impl.checkers.data;

import net.mauhiz.board.model.data.Piece;

public class CheckersPiece implements Piece {

	private final CheckersPieceType piece;
	private CheckersPlayerType player;

	public CheckersPiece(CheckersPlayerType player, CheckersPieceType piece) {
		this.piece = piece;
		this.player = player;
	}

	public CheckersPieceType getPieceType() {
		return piece;
	}

	public CheckersPlayerType getPlayerType() {
		return player;
	}
}
