package net.mauhiz.board.impl.go.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import net.mauhiz.board.impl.common.gui.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.go.data.GoPlayerType;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.PocketBoardGui;

/**
 * @author mauhiz
 */
public class GoSwingAssistant extends PocketSwingGuiAssistant {

	public GoSwingAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(RotatingJButton button, Piece op) {
		if (op == null) {
			button.setText("");
		} else {
			button.setText(op.getPieceType().toString());
			button.setForeground(op.getPlayerType() == GoPlayerType.BLACK ? Color.BLACK : Color.WHITE);
		}
	}

	@Override
	protected GoGui getParent() {
		return (GoGui) super.getParent();
	}

	public void initPockets() {
		JPanel pocket = newPocket();
		frame.add(pocket, 0);
		pockets.put(GoPlayerType.BLACK, pocket);
		pocket = newPocket();
		frame.add(pocket);
		pockets.put(GoPlayerType.WHITE, pocket);
	}

	private JPanel newPocket() {
		JPanel pocket = new JPanel();
		pocket.setLayout(new GridLayout(1, 7));
		pocket.setSize(7 * 30, 30);
		pocket.setToolTipText("Pocket");
		return pocket;
	}
}
