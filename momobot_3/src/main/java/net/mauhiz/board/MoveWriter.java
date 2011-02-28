package net.mauhiz.board;

public interface MoveWriter<B extends Board<? extends Piece, ? extends Player>, M extends Move> {
    String write(B board, M move);
}
