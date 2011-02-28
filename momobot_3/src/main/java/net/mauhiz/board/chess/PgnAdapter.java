package net.mauhiz.board.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.board.MoveReader;
import net.mauhiz.board.MoveWriter;
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

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

public class PgnAdapter implements MoveReader<ChessBoard, ChessMove>, MoveWriter<ChessBoard, ChessMove> {
    private static final Pattern CASTLE = Pattern.compile("O-O(-O)?([+#=])?");
    private static final Pattern SAN_MOVE = Pattern
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
            if (ChessRule.canGo(board, square, to)) {
                return square;
            }
        }
        return null;
    }

    private static Square positionToSquare(char col, char row) {
        return Square.getInstance(col - 'a', row - '1');
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

    @Override
    public ChessMove read(ChessBoard board, String pgnMove) {
        ChessPlayer player = board.getTurn();

        ChessMove cm;
        Matcher castle = CASTLE.matcher(pgnMove);
        if (castle.matches()) {
            cm = new Castle();
            boolean white = player == ChessPlayer.WHITE;
            boolean great = castle.group(1) != null;
            cm.setFrom(Square.getInstance(4, white ? 0 : 7));
            cm.setTo(Square.getInstance(great ? 2 : 6, white ? 0 : 7));
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
                cm.setTo(positionToSquare(toX, toY));
                cm.setFrom(findFrom(board, fromPosition, cm.getTo(), cm.moved, player));
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

        return cm;
    }

    /**
     * @param pgnData no pairs, no comments
     */
    public List<ChessMove> read(String pgnData) {
        String[] pgnMoves = pgnData.split(" ");
        ChessBoard board = new ChessBoard();
        List<ChessMove> moves = new ArrayList<ChessMove>(2 * pgnMoves.length / 3);

        for (int i = 0; i < pgnMoves.length; i++) {
            if (i % 3 == 0) {
                // move #
                continue;
            }

            ChessMove move = read(board, pgnMoves[i]);
            if (!board.move(move)) {
                throw new IllegalArgumentException("Invalid move at " + i / 3);
            }
            moves.add(move);
        }

        return moves;
    }

    public String write(ChessBoard board, ChessMove chessMove) {
        throw new NotImplementedException();
    }
}
