package net.mauhiz.board.shogi.gui;

import java.util.Collection;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.IBoardGui;
import net.mauhiz.board.shogi.model.ShogiBoard;
import net.mauhiz.board.shogi.model.ShogiMove;
import net.mauhiz.board.shogi.model.ShogiOwnedPiece;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.board.shogi.model.ShogiPlayer;

public interface IShogiGui extends IBoardGui<ShogiBoard, ShogiMove> {

    void decorate(Square square, ShogiOwnedPiece op);

    void initPockets();

    void refreshPocket(ShogiBoardController shogiBoardController, ShogiPlayer key, Collection<ShogiPiece> value);

    void showPromotionDialog(ShogiBoardController controller, ShogiOwnedPiece piece);
}
