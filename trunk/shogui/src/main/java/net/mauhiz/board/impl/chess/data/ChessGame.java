package net.mauhiz.board.impl.chess.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.impl.chess.Castle;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

public class ChessGame implements Game {
    private final ChessBoard board;
    private final List<Move> history = new ArrayList<Move>();
    private final ChessRule rule;
    private PlayerType turn;

    public ChessGame(ChessRule rule) {
        this.rule = rule;
        board = rule.newBoard();
        turn = rule.getStartingPlayer();
        rule.initPieces(board);
    }

    @Override
    public PlayerType applyMove(Move move) {
        if (rule.isValid(move, this)) {
            ChessGame clone = copy();
            clone.board.applyMove(move);
            clone.history.add(move);
            if (rule.isCheck(turn, clone)) {
                return null;
            }
            board.applyMove(move);
            history.add(move);
            turn = move.getPlayerType().other();
            return turn;
        }

        return null;
    }

    private ChessGame copy() {
        ChessGame copy = new ChessGame(rule);
        copy.history.addAll(history);
        for (Move move : copy.history) {
            copy.board.applyMove(move);
        }
        copy.turn = turn;
        return copy;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public List<Move> getMoves() {
        return history;
    }

    @Override
    public ChessRule getRule() {
        return rule;
    }

    @Override
    public PlayerType getTurn() {
        return turn;
    }

    public boolean kingHasMoved(PlayerType kingOwner) {
        for (Move move : history) {
            if (move.getPlayerType() == kingOwner) {
                if (move instanceof Castle) {
                    return true;
                } else if (move instanceof NormalMove) {
                    NormalMove nmove = (NormalMove) move;
                    if (nmove.getFrom().getX() == 4
                            && nmove.getFrom().getY() == (kingOwner == ChessPlayerType.WHITE ? 0 : 7)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean rookHasMoved(PlayerType rookOwner, Square rookStart) {
        for (Move move : history) {
            if (move.getPlayerType() == rookOwner && move instanceof NormalMove) {
                NormalMove nmove = (NormalMove) move;
                if (nmove.getFrom() == rookStart) {
                    return true;
                }
            }
        }
        return false;
    }

}
