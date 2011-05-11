package net.mauhiz.board.impl.shogi.gui;

import java.awt.Color;

import net.mauhiz.board.impl.common.gui.AbstractPocketInteractiveBoardGui;
import net.mauhiz.board.impl.shogi.ShogiGameController;
import net.mauhiz.board.impl.shogi.data.ShogiBoard;
import net.mauhiz.board.impl.shogi.data.ShogiRule;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;

public class ShogiGui extends AbstractPocketInteractiveBoardGui {

    public static void main(String[] args) {
        ShogiGui gui = new ShogiGui();
        // gui.assistant = new ShogiSwtAssistant(gui);
        gui.assistant = new ShogiSwingAssistant(gui);
        gui.assistant.start();
    }

    public void afterPromotionDialog(NormalMove move, boolean promote) {
        if (promote) {
            Move promotion = getController().convertToPromotion(move);
            super.sendMove(promotion);
        } else {
            super.sendMove(move);
        }
    }

    IShogiGuiAssistant getAssistant() {
        return (IShogiGuiAssistant) assistant;
    }

    @Override
    public ShogiBoard getBoard() {
        return getController().getGame().getBoard();
    }

    public ShogiGameController getController() {
        return (ShogiGameController) controller;
    }

    @Override
    public ShogiRule getRule() {
        return getController().getGame().getRule();
    }

    @Override
    public Color getSquareBgcolor(Square square) {
        return Color.decode("0xFFCC66");
    }

    @Override
    public String getWindowTitle() {
        return "Shogui";
    }

    @Override
    protected GameController newController() {
        return new ShogiGameController(this);
    }

    @Override
    public synchronized void refresh() {
        super.refresh();
        getAssistant().refreshPockets(getBoard().getAllPocketPieces());
        getAssistant().refresh();
    }

    @Override
    protected void refreshSquare(Square square) {
        Piece piece = getBoard().getPieceAt(square);
        if (selectedSquare == null) {
            if (selectedPiece == null) { // available pieces
                if (piece != null && piece.getPlayerType() == getTurn()) {
                    addSelectAction(square);
                }
            } else { // from the pocket
                Drop drop = getRule().generateMove(selectedPiece, square, controller.getGame());
                if (drop != null) {
                    addMoveAction(square, drop);
                }
            }
        } else { // from the board
            // available destinations
            Move move = getRule().generateMove(selectedSquare, square, controller.getGame());

            if (move != null) {
                addMoveAction(square, move);
            }
        }
    }

    @Override
    public void sendMove(Move move) {
        if (move instanceof NormalMove) {
            if (getRule().canPromote(getBoard(), getSelectedSquare(), ((NormalMove) move).getTo())) {
                showPromotionDialog((NormalMove) move);
                return; // do not send move yet
            }
        }
        super.sendMove(move);
    }

    public void showPromotionDialog(NormalMove move) {
        getAssistant().showPromotionDialog(move);
    }
}
