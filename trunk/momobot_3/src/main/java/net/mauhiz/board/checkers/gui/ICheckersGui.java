package net.mauhiz.board.checkers.gui;

import net.mauhiz.board.Square;
import net.mauhiz.board.checkers.model.CheckersOwnedPiece;
import net.mauhiz.board.gui.IBoardGui;

public interface ICheckersGui extends IBoardGui {

    void decorate(Square square, CheckersOwnedPiece op);
}
