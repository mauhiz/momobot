package net.mauhiz.board.remote;

import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

public class RemoteBoardAdapter implements GameController {

    private GameController localController;
    private MoveReader reader;

    public RemoteBoardAdapter(GameController localController, MoveReader reader) {
        this.localController = localController;
        this.reader = reader;
    }

    @Override
    public BoardIO getBoardIO() {
        return localController.getBoardIO();
    }

    @Override
    public Game getGame() {
        return localController.getGame();
    }

    public void readMove(String moveStr) {
        reader.read(getGame(), moveStr);
    }

    @Override
    public void receiveMove(Move move) {
        BoardManager.getInstance().sendMove(this, move);
        localController.receiveMove(move);
    }

}
