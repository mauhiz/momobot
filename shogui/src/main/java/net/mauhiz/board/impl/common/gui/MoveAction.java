package net.mauhiz.board.impl.common.gui;

import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.gui.InteractiveBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class MoveAction extends AbstractAction {

    private final InteractiveBoardGui gui;
    private final Move move;

    public MoveAction(InteractiveBoardGui gui, Move move) {
        super();
        this.move = move;
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.sendMove(move);
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.GUI_ASYNCHRONOUS;
    }
}
