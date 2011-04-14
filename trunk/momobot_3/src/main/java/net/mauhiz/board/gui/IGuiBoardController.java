package net.mauhiz.board.gui;

import net.mauhiz.board.Square;

public interface IGuiBoardController<B, M> {

    void cancelSelection();

    void movePiece(Square to);

    void selectPiece(Square at);
}
