package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public interface GameController {

	/**
	 * Reflects the move to the board
	 * @param to
	 */
	void receiveMove(Move move);

	/**
	 * Builds a move
	 */
	Move generateMove(PlayerType player, Square from, Square to);
	
	Game getGame();

	BoardIO getBoardIO();
}
