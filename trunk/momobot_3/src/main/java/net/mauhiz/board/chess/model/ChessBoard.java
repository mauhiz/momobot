package net.mauhiz.board.chess.model;

import static java.lang.Math.abs;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Board;
import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public class ChessBoard extends Board {
    public static enum Status {
        CHECK(false), DRAW(true), MATE(true);
        private boolean end;

        private Status(boolean end) {
            this.end = end;
        }

        public boolean isEnd() {
            return end;
        }
    }

    public static final int SIZE = 8;

    static boolean isFrontCorner(Square from, Square to, Player pl) {
        return abs(getXmove(from, to)) == 1 && getYmove(from, to) == (pl == ChessPlayer.WHITE ? 1 : -1);
    }

    private final Map<Square, ChessOwnedPiece> piecesMap = new HashMap<Square, ChessOwnedPiece>();
    private ChessPlayer turn = ChessPlayer.WHITE;

    public boolean canCastle(Square from, Square to, ChessPlayer player) {
        // TODO use move history (and status?)

        return from.x == 4 && abs(getXmove(from, to)) == 2 && getYmove(from, to) == 0
                && from.y == (player == ChessPlayer.WHITE ? 0 : 7) && !isObstruction(from, to) && !isCheck(player);
    }

    public boolean canEnPassant(Square from, Square to, ChessPlayer player) {

        if (isFrontCorner(from, to, player)) {
            Square enPassant = Square.getInstance(to.x, from.y);
            ChessOwnedPiece couic = getOwnedPieceAt(enPassant);
            if (couic == null || couic.getPiece() != ChessPiece.PAWN || couic.getPlayer() == player) {
                return false;
            }

            // TODO use move history
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
            if (op.getPlayer() == pl && op.getPiece() == ChessPiece.KING) {
                return square;
            }
        }

        return null;
    }

    @Override
    public ChessOwnedPiece getOwnedPieceAt(Square square) {
        return piecesMap.get(square);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(SIZE, SIZE);
    }

    @Override
    public ChessPlayer getTurn() {
        return turn;
    }

    public boolean isCheck(Player player) {
        Square kingSquare = findKingSquare(player);
        if (kingSquare == null) {
            return false;
        }

        for (Square square : new SquareView(getSize())) {
            ChessOwnedPiece attacker = getOwnedPieceAt(square);
            if (attacker == null || attacker.getPlayer() == player) {
                continue;
            }
            if (ChessRule.canGo(this, attacker, square, kingSquare)) {
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
        ChessOwnedPiece toMove = getOwnedPieceAt(from);
        if (toMove == null || toMove.getPlayer() != pl) {
            return false;
        }

        if (ChessRule.canGo(this, toMove, from, to)) {
            piecesMap.remove(from);
            ChessOwnedPiece captured = piecesMap.put(to, toMove);

            // castle
            if (toMove.getPiece() == ChessPiece.KING && abs(getXmove(from, to)) == 2) {
                // move the rook too
                Square rookFrom = Square.getInstance(getXmove(from, to) > 0 ? 7 : 0, from.y);
                Square rookTo = Square.getInstance(to.x - getXmove(from, to) / 2, rookFrom.y);

                piecesMap.put(rookTo, piecesMap.remove(rookFrom));

            } else if (toMove.getPiece() == ChessPiece.PAWN && isFrontCorner(from, to, pl) && captured == null) {
                Square enPassant = Square.getInstance(to.x, from.y);
                piecesMap.remove(enPassant);
            }

            turn = turn.next();
            return true;
        }

        return false;
    }

    @Override
    public void newGame() {
        piecesMap.clear();

        for (Square square : new SquareView(getSize())) {
            int j = square.y;
            int i = square.x;
            ChessPlayer pl = j <= 2 ? ChessPlayer.WHITE : ChessPlayer.BLACK;

            if (j == 0 || j == 7) {
                if (i == 0 || i == 7) {
                    piecesMap.put(square, new ChessOwnedPiece(pl, ChessPiece.ROOK));
                } else if (i == 1 || i == 6) {
                    piecesMap.put(square, new ChessOwnedPiece(pl, ChessPiece.KNIGHT));
                } else if (i == 2 || i == 5) {
                    piecesMap.put(square, new ChessOwnedPiece(pl, ChessPiece.BISHOP));
                } else if (i == 3) {
                    piecesMap.put(square, new ChessOwnedPiece(pl, ChessPiece.QUEEN));
                } else if (i == 4) {
                    piecesMap.put(square, new ChessOwnedPiece(pl, ChessPiece.KING));
                }
            } else if (j == 1 || j == 6) {
                piecesMap.put(square, new ChessOwnedPiece(pl, ChessPiece.PAWN));
            }
        }

        turn = ChessPlayer.WHITE;
    }

    public void promote(Square to, ChessPiece promotion) {
        ChessOwnedPiece op = getOwnedPieceAt(to);
        assert op != null;
        op.setPiece(promotion);
    }
}
