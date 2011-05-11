package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class ExitAction extends AbstractAction {
	private final BoardGui gui;

	public ExitAction(BoardGui gui) {
		super();
		this.gui = gui;
	}

	@Override
	protected void doAction() {
		gui.close();
	}

	@Override
	protected ExecutionType getExecutionType() {
		return ExecutionType.NON_GUI;
	}
}
