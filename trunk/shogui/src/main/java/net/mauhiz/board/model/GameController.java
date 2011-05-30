package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

public interface GameController {

	BoardIO getBoardIO();

	Game getGame();

	/**
	 * Reflects the move to the board
	 */
	PlayerType receiveMove(Move move);
}
