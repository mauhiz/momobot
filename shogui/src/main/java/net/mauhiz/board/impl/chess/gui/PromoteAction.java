package net.mauhiz.board.impl.chess.gui;

import net.mauhiz.board.impl.chess.PromoteMove;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

public class PromoteAction extends AbstractAction {
	private final BoardGui lparent;
	private final NormalMove nmove;
	private final ChessPieceType promotion;

	public PromoteAction(NormalMove nmove, BoardGui lparent, ChessPieceType promotion) {
		this.nmove = nmove;
		this.lparent = lparent;
		this.promotion = promotion;
	}

	@Override
	protected ExecutionType getExecutionType() {
		return ExecutionType.GUI_SYNCHRONOUS;
	}

	@Override
	public void trun() {
		Move promoteMove = new PromoteMove(nmove, promotion);
		lparent.sendMove(promoteMove);
	}
}