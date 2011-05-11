package net.mauhiz.board.impl.common;

import net.mauhiz.board.impl.common.data.DropImpl;
import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.PocketGameController;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public abstract class AbstractPocketGameController extends AbstractGameController implements PocketGameController {
	protected AbstractPocketGameController(BoardIO display) {
		super(display);
	}

	@Override
	public Drop generateMove(PlayerType player, PieceType piece, Square to) {
		return new DropImpl(player, piece, to);
	}
}
