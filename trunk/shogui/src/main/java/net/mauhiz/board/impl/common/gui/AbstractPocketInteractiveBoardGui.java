package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;

public abstract class AbstractPocketInteractiveBoardGui extends AbstractInteractiveBoardGui implements PocketBoardGui {

	protected PieceType selectedPiece;

	@Override
	public void selectPieceToDrop(PlayerType player, PieceType piece) {
		selectedPiece = piece;
		
		// TODO modify the action on the originating button
	}
}
