package net.mauhiz.board.chess.gui;

import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Square;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessMove;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.gui.IBoardGui;

public interface IChessGui extends IBoardGui<ChessBoard, ChessMove> {

    void decorate(Square square, OwnedPiece<ChessPiece, ChessPlayer> op);

    void showPromotionDialog(ChessPiece[] promotions, ChessBoardController controller, Square to);
}
