package net.mauhiz.board.impl.shogi;

import net.mauhiz.board.impl.common.AbstractPocketGameController;
import net.mauhiz.board.impl.shogi.data.ShogiGame;
import net.mauhiz.board.impl.shogi.data.ShogiRule;
import net.mauhiz.board.impl.shogi.gui.ShogiGui;
import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.data.NormalMove;

public class ShogiGameController extends AbstractPocketGameController {

	public ShogiGameController(BoardIO display) {
		super(display);
	}

	@Override
	public ShogiGui getBoardIO() {
		return (ShogiGui) super.getBoardIO();
	}

	@Override
	protected ShogiGame newGame() {
		return new ShogiGame(new ShogiRule());
	}

	public ShogiGame getGame() {
		return (ShogiGame) game;
	}

	public PromoteMove convertToPromotion(NormalMove move) {
		return new PromoteMove(move);
	}
}
