package net.mauhiz.board;

public interface Rule<B extends Board<? extends Piece, ? extends Player>> {
    boolean isLegalMove(B board, Move move);
}
