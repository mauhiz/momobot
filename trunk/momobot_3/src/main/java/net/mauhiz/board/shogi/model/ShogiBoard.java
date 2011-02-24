package net.mauhiz.board.shogi.model;

import static java.lang.Math.abs;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.board.Board;
import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public class ShogiBoard extends Board {
    public static final int SIZE = 9;

    static boolean isFrontCorner(Square from, Square to, ShogiPlayer pl) {
        return abs(getXmove(from, to)) == 1 && getYmove(from, to) == (pl == ShogiPlayer.BOTTOM ? 1 : -1);
    }

    private final Map<Square, ShogiOwnedPiece> piecesMap = new HashMap<Square, ShogiOwnedPiece>();
    private final Map<ShogiPlayer, Collection<ShogiPiece>> pockets = new HashMap<ShogiPlayer, Collection<ShogiPiece>>();

    private ShogiPlayer turn = ShogiPlayer.BOTTOM;
    {
        pockets.put(ShogiPlayer.TOP, new ArrayList<ShogiPiece>());
        pockets.put(ShogiPlayer.BOTTOM, new ArrayList<ShogiPiece>());
    }

    public boolean drop(ShogiPlayer player, ShogiPiece piece, Square to) {
        if (player != turn || !ShogiRule.canDrop(this, to)) {
            return false;
        }

        // look in pockets
        if (pockets.get(player).remove(piece)) {
            piecesMap.put(to, new ShogiOwnedPiece(player, piece));
            turn = turn.next();
            return true;
        }

        return false;
    }

    public Square findKingSquare(Player pl) {
        // locate the king
        for (Square square : new SquareView(getSize())) {
            OwnedPiece op = getOwnedPieceAt(square);
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
        return piecesMap.get(square);
    }

    public Iterable<Entry<ShogiPlayer, Collection<ShogiPiece>>> getPockets() {
        return pockets.entrySet();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(SIZE, SIZE);
    }

    @Override
    public ShogiPlayer getTurn() {
        return turn;
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
            if (ShogiRule.canGo(this, attacker, square, kingSquare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean move(Player pl, Square from, Square to) {
        if (!pl.equals(turn)) {
            return false;
        }
        ShogiOwnedPiece toMove = getOwnedPieceAt(from);
        if (toMove == null || toMove.getPlayer() != pl) {
            return false;
        }

        if (ShogiRule.canGo(this, toMove, from, to)) {
            piecesMap.remove(from);
            ShogiOwnedPiece capturedPiece = piecesMap.put(to, toMove);

            if (capturedPiece != null) {
                pockets.get(pl).add(capturedPiece.getPiece());
            }

            turn = turn.next();
            return true;
        }

        return false;
    }

    @Override
    public void newGame() {
        for (Collection<ShogiPiece> pocket : pockets.values()) {
            pocket.clear();
        }
        piecesMap.clear();

        for (Square square : new SquareView(getSize())) {
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

        turn = ShogiPlayer.BOTTOM;
    }
}
