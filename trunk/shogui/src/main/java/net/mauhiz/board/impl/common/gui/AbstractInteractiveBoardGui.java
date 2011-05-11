package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;

import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Rule;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.GuiAssistant;
import net.mauhiz.board.model.gui.InteractiveBoardGui;
import net.mauhiz.util.IAction;

public abstract class AbstractInteractiveBoardGui implements InteractiveBoardGui {
    protected GuiAssistant assistant;
    protected GameController controller;
    protected Square selectedSquare;

    public void addCancelAction(Square square) {
        enableSquare(square, new CancelAction(this));
    }

    public void addMoveAction(Square square, Move move) {
        enableSquare(square, new MoveAction(this, move));
    }

    public void addSelectAction(Square square) {
        enableSquare(square, new SelectSquareAction(this, square));
    }

    public void cancelSelection() {
        selectedSquare = null;
        refresh();
    }

    @Override
    public void close() {
        assistant.close();
    }

    @Override
    public void disableSquare(Square square) {
        assistant.disableSquare(square);
    }

    public void enableSquare(Square square, IAction action) {
        assistant.enableSquare(square, action);
    }

    protected Board getBoard() {
        return controller.getGame().getBoard();
    }

    public Dimension getDefaultSize() {
        return new Dimension(400, 400);
    }

    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    public Rule getRule() {
        return controller.getGame().getRule();
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    public PlayerType getTurn() {
        return controller.getGame().getTurn();
    }

    protected abstract GameController newController();

    @Override
    public void newGame() {
        controller = newController();
        Dimension size = getBoard().getSize();

        assistant.initLayout(size);

        for (Square square : getBoard().getSquares()) {
            assistant.appendSquare(square, size);
        }

        refresh();
    }

    public synchronized void refresh() {
        for (Square square : getBoard().getSquares()) {
            disableSquare(square);
            refreshSquare(square);
            assistant.decorate(square, getBoard().getPieceAt(square));
        }

        if (getSelectedSquare() != null) {
            addCancelAction(getSelectedSquare());
        }
        assistant.refresh();
    }

    protected abstract void refreshSquare(Square square);

    @Override
    public void selectSquare(Square at) {
        selectedSquare = at;
        refresh();
    }

    public void sendMove(Move move) {
        controller.receiveMove(move);
        cancelSelection();
        refresh();
    }
}
