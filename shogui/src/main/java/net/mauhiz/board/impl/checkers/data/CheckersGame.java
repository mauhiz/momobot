package net.mauhiz.board.impl.checkers.data;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.board.impl.checkers.PromoteMove;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;

public class CheckersGame implements Game {
    private final CheckersBoard board;
    private final List<Move> history = new ArrayList<Move>();
    private final CheckersRule rule;
    private PlayerType turn;

    public CheckersGame(CheckersRule rule) {
        this.rule = rule;
        board = rule.newBoard();
        turn = rule.getStartingPlayer();
        rule.initPieces(board);
    }

    @Override
    public PlayerType applyMove(Move move) {
        if (rule.isValid(move, this)) {
            Move realMove = move;

            if (move instanceof NormalMove) {
                NormalMove nmove = (NormalMove) move;
                if (rule.canPromote(nmove)) {
                    realMove = new PromoteMove(nmove);
                }
            }
            history.add(realMove);
            board.applyMove(realMove);
            turn = realMove.getPlayerType().other();
            // TODO can capture again with same piece if capture
            return turn;
        }

        return null;
    }

    @Override
    public CheckersBoard getBoard() {
        return board;
    }

    @Override
    public List<Move> getMoves() {
        return history;
    }

    @Override
    public CheckersRule getRule() {
        return rule;
    }

    @Override
    public PlayerType getTurn() {
        return turn;
    }

}
