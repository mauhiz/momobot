package net.mauhiz.board.impl.go.gui;

import java.awt.Color;

import net.mauhiz.board.impl.common.gui.AbstractPocketInteractiveBoardGui;
import net.mauhiz.board.impl.go.GoGameController;
import net.mauhiz.board.impl.go.data.GoBoard;
import net.mauhiz.board.impl.go.data.GoRule;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public class GoGui extends AbstractPocketInteractiveBoardGui {

    public static void main(String[] args) {
        GoGui gui = new GoGui();
        // gui.assistant = new ShogiSwtAssistant(gui);
        gui.assistant = new GoSwingAssistant(gui);
        gui.assistant.start();
    }

    PocketGuiAssistant getAssistant() {
        return (PocketGuiAssistant) assistant;
    }

    @Override
    public GoBoard getBoard() {
        return getController().getGame().getBoard();
    }

    public GoGameController getController() {
        return (GoGameController) controller;
    }

    @Override
    public GoRule getRule() {
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
        return new GoGameController(this);
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

    }
}
