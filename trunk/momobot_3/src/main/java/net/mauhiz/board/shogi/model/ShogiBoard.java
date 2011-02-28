package net.mauhiz.board.shogi.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.board.AbstractBoard;
import net.mauhiz.board.Move;
import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public class ShogiBoard extends AbstractBoard<ShogiPiece, ShogiPlayer> {
    public static final int SIZE = 9;

    private final Map<ShogiPlayer, Collection<ShogiPiece>> pockets = new HashMap<ShogiPlayer, Collection<ShogiPiece>>();

    {
        pockets.put(ShogiPlayer.TOP, new ArrayList<ShogiPiece>());
        pockets.put(ShogiPlayer.BOTTOM, new ArrayList<ShogiPiece>());
    }

    public Square findKingSquare(Player pl) {
        // locate the king
        for (Square square : new SquareView(getSize())) {
            ShogiOwnedPiece op = getOwnedPieceAt(square);
            if (op == null) {
                continue;
            }
            if (op.getPlayer() == pl && op.getPiece() == ShogiPiece.KING) {
                return square;
            }
        }

        return null;
    }

    @Override
    public ShogiOwnedPiece getOwnedPieceAt(Square square) {
        return super.getOwnedPieceAt(square);
    }

    public Iterable<Entry<ShogiPlayer, Collection<ShogiPiece>>> getPockets() {
        return pockets.entrySet();
    }

    @Override
    public ShogiRule getRule() {
        return super.getRule();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(SIZE, SIZE);
    }

    @Override
    protected void initGameFor(Square square) {
        int j = square.y;
        int i = square.x;
        ShogiPlayer pl = j <= 2 ? ShogiPlayer.BOTTOM : ShogiPlayer.TOP;

        if (j == 0 || j == 8) {
            if (i == 0 || i == 8) {
                piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.LANCE));
            } else if (i == 1 || i == 7) {
                piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.KNIGHT));
            } else if (i == 2 || i == 6) {
                piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.SILVER));
            } else if (i == 3 || i == 5) {
                piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.GOLD));
            } else {
                piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.KING));
            }
        } else if (j == 1 && i == 1 || j == 7 && i == 7) {
            piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.BISHOP));
        } else if (j == 1 && i == 7 || j == 7 && i == 1) {
            piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.ROOK));
        } else if (j == 2 || j == 6) {
            piecesMap.put(square, new ShogiOwnedPiece(pl, ShogiPiece.PAWN));
        }
    }

    @Override
    protected void initTurn() {
        turn = ShogiPlayer.BOTTOM;
    }

    public boolean isCheck(ShogiPlayer player) {
        Square kingSquare = findKingSquare(player);
        if (kingSquare == null) {
            return false;
        }

        for (Square square : new SquareView(getSize())) {
            ShogiOwnedPiece attacker = getOwnedPieceAt(square);
            if (attacker == null || attacker.getPlayer() == player) {
                continue;
            }

            ShogiMove wannabe = new ShogiMove();
            wannabe.setFrom(square);
            wannabe.setTo(kingSquare);

            if (getRule().isLegalMove(this, wannabe)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isForward(Square from, Square to, ShogiPlayer player) {
        return from.y != to.y && player == ShogiPlayer.BOTTOM ^ from.y < to.y;
    }

    public boolean isGoldMove(Square from, Square to, ShogiPlayer player) {
        return isCross(from, to) || isFrontCorner(from, to, player);
    }

    public boolean isPawnMove(Square from, Square to, ShogiPlayer player) {
        return AbstractBoard.getXmove(from, to) == 0 && isForward(from, to, player);
    }

    @Override
    public boolean move(Move move) {
        if (move instanceof ShogiMove) {

            if (getRule().isLegalMove(this, move)) {
                ShogiPlayer player = getTurn();

                if (move instanceof Drop) {

                    ShogiPiece piece = ((Drop) move).dropped;

                    // look in pockets
                    if (pockets.get(player).remove(piece)) {
                        piecesMap.put(move.getTo(), new ShogiOwnedPiece(player, piece));
                        nextTurn();
                        return true;
                    }

                } else {
                    Square from = ((ShogiMove) move).getFrom();
                    ShogiOwnedPiece toMove = getOwnedPieceAt(from);

                    piecesMap.remove(from);
                    OwnedPiece<ShogiPiece, ShogiPlayer> capturedPiece = piecesMap.put(move.getTo(), toMove);

                    if (capturedPiece != null) {
                        pockets.get(player).add(capturedPiece.getPiece());
                    }

                    nextTurn();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void newGame() {
        for (Collection<ShogiPiece> pocket : pockets.values()) {
            pocket.clear();
        }

        super.newGame();
    }
}
