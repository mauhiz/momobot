package net.mauhiz.board.impl.common.action;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.model.gui.InteractiveBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class CancelAction extends AbstractAction {
	private static final Map<InteractiveBoardGui, CancelAction> INSTANCES = new HashMap<InteractiveBoardGui, CancelAction>(
			1);

	public static final CancelAction getInstance(InteractiveBoardGui gui) {
		CancelAction instance = INSTANCES.get(gui);
		if (instance == null) {
			instance = new CancelAction(gui);
			INSTANCES.put(gui, instance);
		}
		return instance;
	}

	private final InteractiveBoardGui gui;

	private CancelAction(InteractiveBoardGui gui) {
		super();
		this.gui = gui;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof CancelAction && isEquals((CancelAction) obj);
	}

	@Override
	public ExecutionType getExecutionType() {
		return ExecutionType.PARALLEL_CACHED;
	}

	@Override
	public int hashCode() {
		return gui.hashCode();
	}

	private boolean isEquals(CancelAction other) {
		return gui == other.gui;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public void trun() {
		gui.cancelSelection();
	}
}
