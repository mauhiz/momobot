package net.mauhiz.board.chess.gui;

import net.mauhiz.board.Square;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.gui.IBoardGui;

public interface IChessGui extends IBoardGui {

    void decorate(Square square, ChessOwnedPiece op);

    void showPromotionDialog(ChessPiece[] promotions, ChessBoardController controller, Square to);
}
