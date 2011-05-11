package net.mauhiz.board.impl.chess.gui;

import net.mauhiz.board.impl.chess.PromoteMove;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessPlayerType;
import net.mauhiz.board.impl.common.gui.SwtGuiAssistant;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ChessGuiAssistant extends SwtGuiAssistant {
	public ChessGuiAssistant(BoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(final Button button, final Piece piece) {
		final Shell lshell = shell;
		button.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if (piece == null) {
					button.setText("");
				} else {
					button.setText(piece.getPieceType().toString());
					Display display = lshell.getDisplay();
					Color black = display.getSystemColor(SWT.COLOR_BLACK);
					Color white = display.getSystemColor(SWT.COLOR_WHITE);
					button.setForeground(piece.getPlayerType() == ChessPlayerType.BLACK ? black : white);
				}
			}
		});

	}
	
	public void showPromotionDialog(ChessPieceType[] promotions, final NormalMove nmove) {
		Shell popup = new Shell(shell, SWT.ICON_QUESTION);
		popup.setLayout(new GridLayout(promotions.length, true));
		popup.setText("Promotion?");
		popup.setActive();
		final BoardGui lparent = parent;

		for (final ChessPieceType promotion : promotions) {
			Button promoButton = new Button(popup, SWT.PUSH);
			promoButton.setText(promotion.getName());
			promoButton.setToolTipText(promotion.name());
			promoButton.addSelectionListener(new AbstractAction() {
				
				@Override
				protected ExecutionType getExecutionType() {
					return ExecutionType.NON_GUI;
				}
				
				@Override
				protected void doAction() {
					Move promoteMove = new PromoteMove(nmove, promotion);
					lparent.sendMove(promoteMove);
				}
			});
		}
		popup.pack();
		popup.open();
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
}
