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
	public boolean equals(Object obj) {
		return this == obj || obj instanceof SelectSquareAction && isEquals((SelectSquareAction) obj);
	}

	@Override
	protected ExecutionType getExecutionType() {
		return ExecutionType.PARALLEL_CACHED;
	}

	@Override
	public int hashCode() {
		return at.hashCode();
	}

	private boolean isEquals(SelectSquareAction other) {
		return at.equals(other.at) && gui == other.gui;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + at;
	}

	@Override
	public void trun() {
		gui.selectSquare(at);
	}
}
