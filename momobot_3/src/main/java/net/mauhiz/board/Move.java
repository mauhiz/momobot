package net.mauhiz.board;

public interface Move<B extends Board> {

    Square getTo();

    String toString(B board);
}
