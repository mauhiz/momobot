package net.mauhiz.board.impl.checkers.data;

import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.impl.common.data.AbstractRule;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

import org.apache.commons.lang.IllegalClassException;

public class CheckersRule extends AbstractRule {

    /**
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    private boolean canGo(CheckersBoard b, Square from, Square to) {
        CheckersPiece op = b.getPieceAt(from);

        if (b.getPieceAt(to) != null) {
            return false;
        }

        if (AbstractBoard.isCornerSkip(from, to)) {
            // capture
            Square skipped = CheckersBoard.getSkippedSquare(from, to);
            CheckersPiece captured = b.getPieceAt(skipped);
            if (captured == null || captured.getPlayerType() == op.getPlayerType()) {
                return false;
            }

        } else if (!AbstractBoard.isCorner(from, to)) {
            return false;
        }

        return isForward(from, to, op.getPlayerType()) || op.getPieceType() == CheckersPieceType.QUEEN;
    }

    boolean canPromote(NormalMove nmove) {
        return nmove.getPlayerType() == CheckersPlayerType.BLACK ? nmove.getTo().getY() == 9
                : nmove.getTo().getY() == 0;
    }

    @Override
    public PlayerType[] getPlayerTypes() {
        return CheckersPlayerType.values();
    }

    @Override
    public PlayerType getStartingPlayer() {
        return CheckersPlayerType.BLACK;
    }

    @Override
    public void initPieces(Board board) {
        for (Square square : board.getSquares()) {
            int j = square.getY();
            int i = square.getX();

            if (j <= 3 || j >= 6) {
                CheckersPlayerType pl = j <= 4 ? CheckersPlayerType.BLACK : CheckersPlayerType.WHITE;

                if ((i + j) % 2 == 0) {
                    board.setPieceAt(square, new CheckersPiece(pl, CheckersPieceType.PAWN));
                }
            }
        }
    }

    @Override
    protected boolean isForward(Square from, Square to, PlayerType player) {
        return from.getY() != to.getY() && player == CheckersPlayerType.BLACK ^ from.getY() > to.getY();
    }

    @Override
    public boolean isValid(Move move, Game game) {
        if (!(game instanceof CheckersGame)) {
            throw new IllegalClassException(CheckersGame.class, game.getClass());
        }
        if (move instanceof NormalMove) {
            return canGo((CheckersBoard) game.getBoard(), ((NormalMove) move).getFrom(), ((NormalMove) move).getTo());
        }

        return false;
    }

    @Override
    public CheckersBoard newBoard() {
        return new CheckersBoard();
    }
}
