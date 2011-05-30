package net.mauhiz.board.impl.shogi.gui;

import java.util.Map;

import net.mauhiz.board.impl.common.gui.PocketSwtGuiAssistant;
import net.mauhiz.board.impl.shogi.data.ShogiPieceType;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.NamedRunnable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ShogiSwtAssistant extends PocketSwtGuiAssistant implements IShogiGuiAssistant {
	static class ButtonDecorator extends NamedRunnable {
		private final Button button;
		private final PieceType piece;
		private final PlayerType player;

		ButtonDecorator(Button button, PieceType piece, PlayerType player) {
			super("Button Decorator");
			this.button = button;
			this.piece = piece;
			this.player = player;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_SYNCHRONOUS;
		}

		@Override
		public void trun() {
			// FIXME disabled buttons ignore setText command
			if (piece == null) {
				button.setText("");
			} else {
				ShogiPieceType pt = (ShogiPieceType) piece;
				if (player == ShogiPlayerType.SENTE) {
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
	public void decorate(Button button, PieceType piece, PlayerType player) {
		new ButtonDecorator(button, piece, player).launch(button.getDisplay());
	}

	@Override
	public ShogiGui getParent() {
		return (ShogiGui) super.getParent();
	}

	public void initPockets() {
		final Shell lshell = shell;
		final Map<PlayerType, Composite> lpockets = pockets;
		new NamedRunnable("Pocket Initializer") {

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			protected void trun() {
				lpockets.put(ShogiPlayerType.GOTE, new Group(lshell, SWT.TOP));
				lpockets.put(ShogiPlayerType.SENTE, new Group(lshell, SWT.BOTTOM));
			}
		}.launch(shell.getDisplay());
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
