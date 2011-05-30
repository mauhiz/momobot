package net.mauhiz.board.impl.common.data;

import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public class DropImpl extends AbstractMove implements Drop {
	private final PieceType pieceType;
	private final Square to;

	public DropImpl(PlayerType playerType, PieceType pieceType, Square to) {
		super(playerType);
		this.to = to;
		this.pieceType = pieceType;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public Square getTo() {
		return to;
	}

	@Override
	public String toString() {
		return pieceType + "@" + to;
	}
}
