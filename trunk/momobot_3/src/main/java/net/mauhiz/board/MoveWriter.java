package net.mauhiz.board;

public interface MoveWriter<B extends Board, M extends Move<B>> {
    String write(B board, M move);
}
