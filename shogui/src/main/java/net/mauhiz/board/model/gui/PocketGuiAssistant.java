package net.mauhiz.board.model.gui;

import java.util.Collection;

import net.mauhiz.board.model.data.Piece;

public interface PocketGuiAssistant extends GuiAssistant {

	void initPockets();

	void addToPocket(Piece piece);

	void refreshPockets(Collection<? extends Piece> contents);
}
