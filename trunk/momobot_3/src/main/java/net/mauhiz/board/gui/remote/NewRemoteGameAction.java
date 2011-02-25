package net.mauhiz.board.gui.remote;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.gui.GuiBoardController;
import net.mauhiz.board.gui.IBoardGui;
import net.mauhiz.board.gui.StartAction;

/**
 * @author mauhiz
 */
public class NewRemoteGameAction<B extends Board, M extends Move<B>> extends StartAction<B, M> {

    public NewRemoteGameAction(IBoardGui<B, M> gui, GuiBoardController<B, M> controller) {
        super(gui, new RemoteBoardAdapter<B, M>(controller));
    }
}
