package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

public interface GameController {

    BoardIO getBoardIO();

    Game getGame();

    /**
     * Reflects the move to the board
     */
    void receiveMove(Move move);
}
