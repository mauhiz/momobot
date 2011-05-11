package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.model.gui.InteractiveBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class CancelAction extends AbstractAction {

	private final InteractiveBoardGui gui;

	public CancelAction(InteractiveBoardGui gui) {
		super();
		this.gui = gui;
	}

	@Override
	protected void doAction() {
		gui.cancelSelection();
	}

	@Override
	protected ExecutionType getExecutionType() {
		return ExecutionType.NON_GUI;
	}
}
