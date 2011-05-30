package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.impl.common.data.CaptureMove;
import net.mauhiz.board.impl.shogi.PromoteMove;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public abstract class AbstractPocketInteractiveBoardGui extends AbstractInteractiveBoardGui implements PocketBoardGui {

	protected PieceType selectedPiece;

	@Override
	public void addSelectAction(Square square) {
		selectedPiece = null;
		super.addSelectAction(square);
	}

	@Override
	public void cancelSelection() {
		selectPieceToDrop(null, null);
	}

	@Override
	protected PocketGuiAssistant getAssistant() {
		return (PocketGuiAssistant) super.getAssistant();
	}

	protected boolean isPieceSelected() {
		return selectedPiece != null;
	}

	public void selectPieceToDrop(PlayerType player, PieceType piece) {
		selectedPiece = piece;
		super.cancelSelection();
	}

	@Override
	public PlayerType sendMove(Move move) {
		PlayerType nextTurn = super.sendMove(move);
		if (nextTurn != null) {
			Move pockMove = move;
			if (move instanceof PromoteMove) {
				pockMove = ((PromoteMove) move).getParentMove();
			}
			if (pockMove instanceof Drop) {
				getAssistant().removeFromPocket(((Drop) pockMove).getPieceType(), pockMove.getPlayerType());
			}
			if (pockMove instanceof CaptureMove) {
				getAssistant().addToPocket(((CaptureMove) pockMove).getCaptured(), pockMove.getPlayerType());
			}
		}
		return nextTurn;
	}
}
