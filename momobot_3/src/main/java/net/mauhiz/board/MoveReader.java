package net.mauhiz.board;


public interface MoveReader<B extends Board, M extends Move<B>> {
    M read(B board, String moveStr);
}
