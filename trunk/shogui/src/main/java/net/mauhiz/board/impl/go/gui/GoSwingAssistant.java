package net.mauhiz.board.impl.go.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import net.mauhiz.board.impl.common.gui.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.go.data.GoPlayerType;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;

/**
 * @author mauhiz
 */
public class GoSwingAssistant extends PocketSwingGuiAssistant {

	public GoSwingAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(RotatingJButton button, PieceType op, PlayerType player) {
		if (op == null) {
			button.setText("");
		} else {
			button.setText(op.toString());
			button.setForeground(player == GoPlayerType.BLACK ? Color.BLACK : Color.WHITE);
		}
	}

	@Override
	protected GoGui getParent() {
		return (GoGui) super.getParent();
	}

	public void initPockets() {
		JPanel pocket = newPocket();
		boardAndPocketsPanel.add(pocket, 0);
		pockets.put(GoPlayerType.BLACK, pocket);
		pocket = newPocket();
		boardAndPocketsPanel.add(pocket);
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
