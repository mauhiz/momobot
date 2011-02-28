package net.mauhiz.board;

public interface OwnedPiece<P extends Piece, J extends Player> {
    P getPiece();

    J getPlayer();

    String getSymbol();
}
