package net.mauhiz.board.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;
import net.mauhiz.board.chess.model.Castle;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessBoard.Status;
import net.mauhiz.board.chess.model.ChessMove;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.chess.model.ChessRule;

import org.apache.commons.lang.StringUtils;

public class PgnReader {
    private static Pattern CASTLE = Pattern.compile("O-O(-O)?([+#=])?");
    private static Pattern SAN_MOVE = Pattern
            .compile("([a-hRNBQKP])?([a-h]?[1-8]?)?(x)?([a-h])([1-8])(=[RNBQ])?([+#=])?");

    private static Square findFrom(ChessBoard board, String fromPosition, Square to, ChessPiece moved,
            ChessPlayer player) {
        if (fromPosition != null && fromPosition.length() == 2) {
            return positionToSquare(fromPosition.charAt(0), fromPosition.charAt(2));
        }
        char position = fromPosition == null ? 0 : fromPosition.charAt(0);
        int col = 0;
        int row = 0;
        if (Character.isDigit(position)) {
            row = position - '1';
        } else {
            col = position - 'a';
        }
        for (Square square : new SquareView(board.getSize())) {
            if (col > 0 && square.x != col || row != 0 && square.y != row) {
                continue;
            }
            ChessOwnedPiece cop = board.getOwnedPieceAt(square);
            if (cop == null || cop.getPlayer() != player || cop.getPiece() != moved) {
                continue;
            }
            if (ChessRule.canGo(board, cop, square, to)) {
                return square;
            }
        }
        return null;
    }

    private static Square positionToSquare(char col, char row) {
        return Square.getInstance(col - 'a', row - '1');
    }

    /**
     * @param pgnData no pairs, no comments
     */
    public static List<ChessMove> read(String pgnData) {
        String[] pgnMoves = pgnData.split(" ");
        ChessBoard board = new ChessBoard();
        List<ChessMove> moves = new ArrayList<ChessMove>();
        for (int i = 0; i < pgnMoves.length; i++) {
            if (i % 3 == 0) {
                // move #
                continue;
            } else if (i % 3 == 1) {
                // white move
                readSingleMove(board, pgnMoves[i], ChessPlayer.WHITE);
            } else if (i % 3 == 2) {
                // black move
                readSingleMove(board, pgnMoves[i], ChessPlayer.BLACK);
            }
        }
        return moves;
    }

    public static ChessMove readSingleMove(ChessBoard board, String pgnMove, ChessPlayer player) {

        ChessMove cm;
        Matcher castle = CASTLE.matcher(pgnMove);
        if (castle.matches()) {
            cm = new Castle();
            boolean white = player == ChessPlayer.WHITE;
            boolean great = castle.group(1) != null;
            cm.from = Square.getInstance(4, white ? 0 : 7);
            cm.to = Square.getInstance(great ? 2 : 6, white ? 0 : 7);
            cm.moved = ChessPiece.KING;
            String statusLine = castle.group(2);
            cm.status = readStatus(statusLine);
        } else {
            Matcher m = SAN_MOVE.matcher(pgnMove);
            if (m.matches()) {

                cm = new ChessMove();
                int i = 0;
                String movedPiece = m.group(++i);
                cm.moved = ChessPiece.fromName(movedPiece == null ? "P" : movedPiece);
                String fromPosition = m.group(++i);
                cm.setCapture(m.group(++i) != null);
                char toX = m.group(++i).charAt(0);
                char toY = m.group(++i).charAt(0);
                cm.to = positionToSquare(toX, toY);
                cm.from = findFrom(board, fromPosition, cm.to, cm.moved, player);
                String promotionType = m.group(++i);
                if (promotionType != null) {
                    cm.promotion = ChessPiece.fromName(promotionType.substring(1));
                }
                String statusLine = m.group(++i);
                cm.status = readStatus(statusLine);
            } else {
                return null;
            }
        }

        cm.player = player;
        return cm;
    }

    private static Status readStatus(String statusLine) {
        if (statusLine != null) {
            if (StringUtils.contains(statusLine, '#')) {
                return Status.MATE;
            } else if (StringUtils.contains(statusLine, '+')) {
                return Status.CHECK;
            } else if (StringUtils.contains(statusLine, '=')) {
                return Status.DRAW;
            }
        }

        return null;
    }

    public static char[] squareToPosition(Square square) {
        return new char[] { (char) (square.x + 'a'), (char) (square.y + '1') };
    }
}
