package net.mauhiz.board.model.data;

public interface Drop extends Move {
	Square getTo();
	PieceType getPieceType();
}
