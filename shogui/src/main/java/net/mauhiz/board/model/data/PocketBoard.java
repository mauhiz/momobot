package net.mauhiz.board.model.data;

import java.util.List;

public interface PocketBoard extends Board {
	List<? extends PieceType> getPocket(PlayerType player);
}
