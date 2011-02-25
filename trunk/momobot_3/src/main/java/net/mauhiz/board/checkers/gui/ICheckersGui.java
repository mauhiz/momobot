package net.mauhiz.board.checkers.gui;

import net.mauhiz.board.Square;
import net.mauhiz.board.checkers.model.CheckersBoard;
import net.mauhiz.board.checkers.model.CheckersMove;
import net.mauhiz.board.checkers.model.CheckersOwnedPiece;
import net.mauhiz.board.gui.IBoardGui;

public interface ICheckersGui extends IBoardGui<CheckersBoard, CheckersMove> {

    void decorate(Square square, CheckersOwnedPiece op);
}
