package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.InteractiveBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class MoveAction extends AbstractAction {

	private final InteractiveBoardGui gui;
	private final GameController controller;
	private final Square to;
	private final PlayerType playerType;

	public MoveAction(GameController controller, Square to, PlayerType player, InteractiveBoardGui gui) {
		super();
		this.to = to;
		this.controller = controller;
		this.playerType = player;
		this.gui = gui;
	}

	@Override
	protected void doAction() {
		Move move = controller.generateMove(playerType, gui.getSelectedSquare(), to);
		gui.sendMove(move);
	}

	@Override
	protected ExecutionType getExecutionType() {
		return ExecutionType.GUI_ASYNCHRONOUS;
	}
}
