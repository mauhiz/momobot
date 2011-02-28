package net.mauhiz.board;

public interface IBoardController<B extends Board<? extends Piece, ? extends Player>, M extends Move> {

    void doMove(M move);

    B getBoard();

    void init();

    MoveReader<B, M> newMoveReader();
}
