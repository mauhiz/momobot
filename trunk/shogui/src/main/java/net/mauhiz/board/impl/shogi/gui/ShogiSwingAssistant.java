package net.mauhiz.board.impl.shogi.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.mauhiz.board.impl.common.gui.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

/**
 * @author mauhiz
 */
public class ShogiSwingAssistant extends PocketSwingGuiAssistant implements IShogiGuiAssistant {

	public ShogiSwingAssistant(BoardGui parent) {
		super(parent);
	}

	public void decorate(RotatingJButton button, Piece op) {
		if (op == null) {
			button.setText("", false);
		} else {
			button.setText(op.getPieceType().toString(), op.getPlayerType() == ShogiPlayerType.GOTE);
		}
	}

	protected ShogiGui getParent() {
		return (ShogiGui) parent;
	}
	
	private JPanel newPocket() {
		JPanel pocket = new JPanel();
		pocket.setLayout(new GridLayout(1, 7));
		pocket.setSize(7 * 30, 30);
		pocket.setToolTipText("Pocket");
		return pocket;
	}

	public void initPockets() {
		JPanel pocket = newPocket();
		frame.add(pocket, 0);
		pockets.put(ShogiPlayerType.SENTE, pocket);
		pocket = newPocket();
		frame.add(pocket);
		pockets.put(ShogiPlayerType.GOTE, pocket);
	}

	public void showPromotionDialog(final NormalMove move) {
		final JDialog popup = new JDialog(frame, "Promotion?");
		popup.setLayout(new GridLayout(1, 2, 10, 10));
		popup.setModal(true);

		JButton promoButton = new JButton("Yes");
		promoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getParent().afterPromotionDialog(move, true);
				popup.dispose();
			}
		});
		popup.add(promoButton);

		JButton cancelButton = new JButton("No");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getParent().afterPromotionDialog(move, false);
				popup.dispose();
			}
		});
		popup.add(cancelButton);

		popup.pack();
		popup.setVisible(true);
	}
}
