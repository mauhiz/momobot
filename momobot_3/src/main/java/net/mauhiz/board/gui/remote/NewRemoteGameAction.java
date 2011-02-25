package net.mauhiz.board.gui.remote;

import net.mauhiz.board.gui.BoardController;
import net.mauhiz.board.gui.IBoardGui;
import net.mauhiz.board.gui.StartAction;

/**
 * @author mauhiz
 */
public class NewRemoteGameAction extends StartAction {

    public NewRemoteGameAction(IBoardGui gui, BoardController controller) {
        super(gui, new RemoteBoardAdapter(controller));
    }
}
