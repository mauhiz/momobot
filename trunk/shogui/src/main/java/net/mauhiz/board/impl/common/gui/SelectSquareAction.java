package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.InteractiveBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class SelectSquareAction extends AbstractAction {

	private final Square at;
	private final InteractiveBoardGui gui;

	public SelectSquareAction(InteractiveBoardGui gui, Square at) {
		super();
		this.gui = gui;
		this.at = at;
	}

	@Override
	protected void doAction() {
		gui.selectSquare(at);
	}

	@Override
	protected ExecutionType getExecutionType() {
		return ExecutionType.NON_GUI;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + at;
	}
}
