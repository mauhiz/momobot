package net.mauhiz.board.gui.remote;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.BoardController;

public class RemoteBoardAdapter extends BoardController {

    private final BoardController localController;

    public RemoteBoardAdapter(BoardController localController) {
        super();
        this.localController = localController;
    }

    @Override
    public void movePiece(Square to) {
        localController.movePiece(to);
        // sendMove(to);
    }

    @Override
    public void refresh() {
        localController.refresh();
    }

    @Override
    public void selectPiece(Square at) {
        localController.selectPiece(at);
    }

}
