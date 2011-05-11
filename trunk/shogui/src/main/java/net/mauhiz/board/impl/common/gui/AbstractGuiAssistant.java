package net.mauhiz.board.impl.common.gui;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.board.model.gui.GuiAssistant;
import net.mauhiz.util.IAction;

public abstract class AbstractGuiAssistant implements GuiAssistant {

	protected BoardGui parent;

	public AbstractGuiAssistant(BoardGui parent) {
		this.parent = parent;
	}

	protected final Map<Square, IAction> listeners = new HashMap<Square, IAction>();
}
