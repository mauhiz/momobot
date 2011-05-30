package net.mauhiz.board.impl.common.data;

import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public class CaptureMove extends NormalMoveImpl {

	private final PieceType captured;

	public CaptureMove(PlayerType playerType, Square from, Square to, PieceType captured) {
		super(playerType, from, to);
		this.captured = captured;
	}

	public PieceType getCaptured() {
		return captured;
	}

}
