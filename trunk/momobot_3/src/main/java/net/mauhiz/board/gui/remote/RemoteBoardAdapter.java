package net.mauhiz.board.gui.remote;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.board.MoveReader;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.gui.AbstractBoardController;

public class RemoteBoardAdapter<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends
        AbstractBoardController<B, M> {

    private final IBoardController<B, M> localController;
    private final MoveReader<B, M> moveReader;

    public RemoteBoardAdapter(IBoardController<B, M> localController) {
        super(localController.getBoard());
        this.localController = localController;
        moveReader = newMoveReader();
    }

    @Override
    public void doMove(M move) {
        localController.doMove(move);
        BoardManager.getInstance().sendMove(this, move);
    }

    @Override
    public void init() {
        localController.init();
    }

    @Override
    public MoveReader<B, M> newMoveReader() {
        return localController.newMoveReader();
    }

    public void readMove(String moveStr) {
        M move = moveReader.read(localController.getBoard(), moveStr);
        localController.doMove(move);
    }

}
