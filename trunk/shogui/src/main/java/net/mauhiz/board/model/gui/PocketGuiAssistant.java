package net.mauhiz.board.model.gui;

import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;

public interface PocketGuiAssistant extends GuiAssistant {

	void addToPocket(PieceType piece, PlayerType player);

	void clearPockets();

	void initPockets();

	void refreshPocketActions(PlayerType player);

	void removeFromPocket(PieceType piece, PlayerType player);

}
