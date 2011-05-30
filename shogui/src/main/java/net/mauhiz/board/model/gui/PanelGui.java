package net.mauhiz.board.model.gui;

import java.awt.Dimension;

public interface PanelGui {

	void close();

	Dimension getDefaultSize();

	Dimension getMinimumSize();

	String getWindowTitle();
}
