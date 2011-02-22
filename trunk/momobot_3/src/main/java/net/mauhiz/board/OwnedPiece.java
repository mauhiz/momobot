package net.mauhiz.board;

public interface OwnedPiece {
    Piece getPiece();

    Player getPlayer();

    String getSymbol();
}
