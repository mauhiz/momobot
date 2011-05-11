package net.mauhiz.board.model.gui;

import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;

public interface PocketBoardGui extends BoardGui {
	void selectPieceToDrop(PlayerType player, PieceType piece);
}
