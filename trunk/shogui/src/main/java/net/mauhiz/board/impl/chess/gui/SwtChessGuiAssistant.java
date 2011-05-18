package net.mauhiz.board.impl.chess.gui;

import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessPlayerType;
import net.mauhiz.board.impl.common.gui.SwtGuiAssistant;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class SwtChessGuiAssistant extends SwtGuiAssistant implements IChessGuiAssistant {
	static class ButtonDecorator implements Runnable {
		private final Button button;
		private final Piece piece;
		private final Shell shell;

		ButtonDecorator(Button button, Shell shell, Piece piece) {
			this.button = button;
			this.shell = shell;
			this.piece = piece;
		}

		@Override
		public void run() {
			if (piece == null) {
				button.setText("");
			} else {
				button.setText(piece.getPieceType().toString());
				Display display = shell.getDisplay();
				Color black = display.getSystemColor(SWT.COLOR_BLACK);
				Color white = display.getSystemColor(SWT.COLOR_WHITE);
				button.setForeground(piece.getPlayerType() == ChessPlayerType.BLACK ? black : white);
			}
		}
	}

	public SwtChessGuiAssistant(BoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(Button button, Piece piece) {
		button.getDisplay().syncExec(new ButtonDecorator(button, shell, piece));
	}

	public boolean showPromotionDialog() {
		MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		mb.setMessage("Promote?");
		int buttonID = mb.open();
		switch (buttonID) {
		case SWT.YES:
			return true;
		default:
			return false;
		}
	}

	public void showPromotionDialog(ChessPieceType[] promotions, NormalMove nmove) {
		Shell popup = new Shell(shell, SWT.ICON_QUESTION);
		popup.setLayout(new GridLayout(promotions.length, true));
		popup.setText("Promotion?");
		popup.setActive();

		for (final ChessPieceType promotion : promotions) {
			Button promoButton = new Button(popup, SWT.PUSH);
			promoButton.setText(promotion.getName());
			promoButton.setToolTipText(promotion.name());
			promoButton.addSelectionListener(new PromoteAction(nmove, getParent(), promotion));
		}
		popup.pack();
		popup.open();
	}
}
