package net.mauhiz.board.model.gui;

import java.awt.Dimension;

public interface PanelGui {

	Dimension getDefaultSize();

	Dimension getMinimumSize();

	void close();
	
	String getWindowTitle();
}
