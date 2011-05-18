package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;

import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Board;
import net.mauhiz.board.model.data.Game;
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
		selectSquare(null);
	}

	@Override
	public void close() {
		getAssistant().close();
	}

	@Override
	public void disableSquare(Square square) {
		getAssistant().disableSquare(square);
	}

	public void enableSquare(Square square, IAction action) {
		getAssistant().enableSquare(square, action);
	}

	protected GuiAssistant getAssistant() {
		return assistant;
	}

	protected Board getBoard() {
		return getGame().getLastBoard();
	}

	protected GameController getController() {
		return controller;
	}

	public Dimension getDefaultSize() {
		return new Dimension(400, 400);
	}

	protected Game getGame() {
		return getController().getGame();
	}

	public Dimension getMinimumSize() {
		return new Dimension(0, 0);
	}

	protected Rule getRule() {
		return getGame().getRule();
	}

	public Square getSelectedSquare() {
		return selectedSquare;
	}

	public PlayerType getTurn() {
		return getGame().getTurn();
	}

	protected boolean isSquareSelected() {
		return getSelectedSquare() != null;
	}

	protected abstract GameController newController();

	@Override
	public void newGame() {
		controller = newController();
		Dimension size = getBoard().getSize();

		getAssistant().initLayout(size);

		for (Square square : getBoard().getSquares()) {
			getAssistant().appendSquare(square, size);
		}

		refresh();
	}

	public void refresh() {
		for (Square square : getBoard().getSquares()) {
			disableSquare(square);
			refreshSquare(square);
			getAssistant().decorate(square, getBoard().getPieceAt(square));
		}

		if (isSquareSelected()) {
			addCancelAction(getSelectedSquare());
		}
		getAssistant().refresh();
	}

	protected abstract void refreshSquare(Square square);

	@Override
	public void selectSquare(Square at) {
		selectedSquare = at;
		refresh();
	}

	public void sendMove(Move move) {
		getController().receiveMove(move);
		cancelSelection();
	}
}
