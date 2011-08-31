package net.mauhiz.board.impl.common.assistant;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.board.model.gui.GuiAssistant;
import net.mauhiz.util.IAction;

public abstract class AbstractGuiAssistant implements GuiAssistant {

	private final Map<Square, IAction> listeners = new HashMap<Square, IAction>();

	protected BoardGui parent;

	public AbstractGuiAssistant(BoardGui parent) {
		this.parent = parent;
	}

	protected void clearListeners() {
		synchronized (listeners) {
			listeners.clear();
		}
	}

	protected BoardGui getParent() {
		return parent;
	}

	protected IAction putListener(Square square, IAction action) {
		synchronized (listeners) {
			return listeners.put(square, action);
		}
	}

	protected IAction removeListener(Square square) {
		synchronized (listeners) {
			return listeners.remove(square);
		}
	}
}
