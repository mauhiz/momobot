package net.mauhiz.board.impl.checkers.gui;

import java.awt.Color;

import net.mauhiz.board.impl.checkers.CheckersGameController;
import net.mauhiz.board.impl.checkers.data.CheckersBoard;
import net.mauhiz.board.impl.checkers.data.CheckersRule;
import net.mauhiz.board.impl.common.gui.AbstractInteractiveBoardGui;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;

/**
 * @author mauhiz
 */
public class CheckersGui extends AbstractInteractiveBoardGui {
	public static void main(String... args) {
		CheckersGui gui = new CheckersGui();
		gui.assistant = new CheckersSwingAssistant(gui);
		gui.getAssistant().start();
	}

	@Override
	protected CheckersBoard getBoard() {
		return (CheckersBoard) super.getBoard();
	}

	@Override
	public CheckersRule getRule() {
		return (CheckersRule) super.getRule();
	}

	public Color getSquareBgcolor(Square square) {
		return (square.getX() + square.getY()) % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY;
	}

	public String getWindowTitle() {
		return "mauhiz' Checkers";
	}

	@Override
	protected CheckersGameController newController() {
		return new CheckersGameController(this);
	}

	@Override
	protected void refreshSquare(Square square) {
		Piece op = getBoard().getPieceAt(square);

		if (isSquareSelected()) { // from the board
			// available destinations
			Move move = getRule().generateMove(getSelectedSquare(), square, getGame());

			if (move != null) {
				addMoveAction(square, move);
				return;
			}
		} else if (op != null && op.getPlayerType() == getTurn()) { // available pieces
			addSelectAction(square);
			return;
		}

		disableSquare(square);
	}
}