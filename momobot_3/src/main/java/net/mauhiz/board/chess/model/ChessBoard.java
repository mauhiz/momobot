package net.mauhiz.board.chess.model;

import static java.lang.Math.abs;

import java.awt.Dimension;

import net.mauhiz.board.AbstractBoard;
import net.mauhiz.board.Move;
import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public class ChessBoard extends AbstractBoard<ChessPiece, ChessPlayer> {
    public static enum Status {
        CHECK(false), DRAW(true), MATE(true);
        private final boolean end;

        private Status(boolean end) {
            this.end = end;
        }

        public boolean isEnd() {
            return end;
        }
    }

    public static final int SIZE = 8;

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
            ChessOwnedPiece op = getOwnedPieceAt(square);
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
        return super.getOwnedPieceAt(square);
    }

    @Override
    public ChessRule getRule() {
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

    @Override
    protected void initTurn() {
        turn = ChessPlayer.WHITE;
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
            if (ChessRule.canGo(this, square, kingSquare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isForward(Square from, Square to, ChessPlayer player) {
        return from.y != to.y && player == ChessPlayer.WHITE ^ from.y > to.y;
    }

    public boolean isPawnMove(Square from, Square to, ChessPlayer player) {
        if (AbstractBoard.getXmove(from, to) == 0 && isForward(from, to, player)) {
            return from.y == (player == ChessPlayer.WHITE ? 1 : 6) ? abs(AbstractBoard.getYmove(from, to)) <= 2
                    : abs(AbstractBoard.getYmove(from, to)) == 1;
        }
        return false;
    }

    @Override
    public boolean move(Move move) {
        if (!(move instanceof ChessMove)) {
            return false;
        }

        Square from = ((ChessMove) move).getFrom();
        ChessOwnedPiece toMove = getOwnedPieceAt(from);

        if (toMove == null || toMove.getPlayer() != getTurn()) {
            return false;
        }

        if (getRule().isLegalMove(this, move)) {
            piecesMap.remove(from);
            Square to = move.getTo();
            OwnedPiece<ChessPiece, ChessPlayer> captured = piecesMap.put(to, toMove);

            // castle
            if (toMove.getPiece() == ChessPiece.KING && abs(getXmove(from, to)) == 2) {
                // move the rook too
                Square rookFrom = Square.getInstance(getXmove(from, to) > 0 ? 7 : 0, from.y);
                Square rookTo = Square.getInstance(to.x - getXmove(from, to) / 2, rookFrom.y);

                piecesMap.put(rookTo, piecesMap.remove(rookFrom));

            } else if (toMove.getPiece() == ChessPiece.PAWN && isFrontCorner(from, to, toMove.getPlayer())
                    && captured == null) {
                Square enPassant = Square.getInstance(to.x, from.y);
                piecesMap.remove(enPassant);
            }

            nextTurn();
            return true;
        }

        return false;
    }

    public void promote(Square to, ChessPiece promotion) {
        ChessOwnedPiece op = getOwnedPieceAt(to);
        assert op != null;
        op.setPiece(promotion);
    }
}
