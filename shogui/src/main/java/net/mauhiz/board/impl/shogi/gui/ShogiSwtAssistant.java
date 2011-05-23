package net.mauhiz.board.impl.shogi.gui;

import net.mauhiz.board.impl.common.gui.PocketSwtGuiAssistant;
import net.mauhiz.board.impl.shogi.data.ShogiPieceType;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.PocketBoardGui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;

public class ShogiSwtAssistant extends PocketSwtGuiAssistant implements IShogiGuiAssistant {
	static class ButtonDecorator implements Runnable {
		private final Button button;
		private final Piece piece;

		ButtonDecorator(Button button, Piece piece) {
			this.button = button;
			this.piece = piece;
		}

		public void run() {
			if (piece == null) {
				button.setText("");
			} else {
				ShogiPieceType pt = (ShogiPieceType) piece.getPieceType();
				if (piece.getPlayerType() == ShogiPlayerType.SENTE) {
					button.setText("^\r\n" + pt.getName());
				} else {
					button.setText(pt.getName() + "\r\nV");
				}
			}
		}
	}

	public ShogiSwtAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(final Button button, final Piece piece) {
		button.getDisplay().syncExec(new ButtonDecorator(button, piece));
	}

	@Override
	public ShogiGui getParent() {
		return (ShogiGui) super.getParent();
	}

	public void initPockets() {
		pockets.put(ShogiPlayerType.GOTE, new Group(shell, SWT.TOP));
		pockets.put(ShogiPlayerType.SENTE, new Group(shell, SWT.BOTTOM));
	}

	public void showPromotionDialog(NormalMove move) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		mb.setMessage("Promote?");
		int buttonID = mb.open();
		switch (buttonID) {
		case SWT.YES:
			getParent().afterPromotionDialog(move, true);
			break;
		default:
			getParent().afterPromotionDialog(move, false);
		}
	}
}
