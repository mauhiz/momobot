package net.mauhiz.board;

public interface MoveReader<B extends Board<? extends Piece, ? extends Player>, M extends Move> {
    M read(B board, String moveStr);
}
