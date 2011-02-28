package net.mauhiz.board.gui.remote;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.gui.AbstractBoardController;
import net.mauhiz.board.gui.IBoardGui;
import net.mauhiz.board.gui.StartAction;

/**
 * @author mauhiz
 */
public class NewRemoteGameAction<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends
        StartAction<B, M> {

    public NewRemoteGameAction(IBoardGui<B, M> gui, AbstractBoardController<B, M> controller) {
        super(gui, new RemoteBoardAdapter<B, M>(controller));
    }
}
