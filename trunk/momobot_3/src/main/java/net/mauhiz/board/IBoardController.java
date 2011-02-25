package net.mauhiz.board;


public interface IBoardController<B extends Board, M extends Move<B>> {

    void doMove(M move);

    B getBoard();

    void init();

    MoveReader<B, M> newMoveReader();
}
