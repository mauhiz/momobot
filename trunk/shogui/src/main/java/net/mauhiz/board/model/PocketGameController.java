package net.mauhiz.board.model;

import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public interface PocketGameController extends GameController {
	Drop generateMove(PlayerType player, PieceType piece, Square square);
}
