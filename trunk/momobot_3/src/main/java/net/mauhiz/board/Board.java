package net.mauhiz.board;

import java.awt.Dimension;

public interface Board<P extends Piece, J extends Player> {

    <O extends OwnedPiece<P, J>> O getOwnedPieceAt(Square to);

    <R extends Rule<? extends Board<P, J>>> R getRule();

    abstract Dimension getSize();

    abstract J getTurn();

    boolean isFriendlyPieceOn(J player, Square to);

    boolean isObstruction(Square from, Square to);

    /**
     * @return true if move was allowed
     */
    <M extends Move> boolean move(M move);

    void newGame();

    <R extends Rule<? extends Board<P, J>>> void setRule(R rule);
}
