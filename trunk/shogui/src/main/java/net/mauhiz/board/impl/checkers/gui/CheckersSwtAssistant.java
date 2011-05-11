package net.mauhiz.board.impl.checkers.gui;

import net.mauhiz.board.impl.checkers.data.CheckersPiece;
import net.mauhiz.board.impl.checkers.data.CheckersPlayerType;
import net.mauhiz.board.impl.common.gui.SwtGuiAssistant;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class CheckersSwtAssistant extends SwtGuiAssistant {

	public CheckersSwtAssistant(BoardGui parent) {
		super(parent);
	}

	@Override
	protected void decorate(Button button, Piece piece) {
		if (piece == null) {
			button.setText("");
		} else {
			CheckersPiece op = (CheckersPiece) piece;
			button.setText(op.getPieceType().getName());

			Display display = shell.getDisplay();
			Color black = display.getSystemColor(SWT.COLOR_BLACK);
			Color white = display.getSystemColor(SWT.COLOR_WHITE);
			button.setForeground(op.getPlayerType() == CheckersPlayerType.BLACK ? black : white);
		}
	}

}
