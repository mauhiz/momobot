package net.mauhiz.board.impl.common.assistant.applet;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import net.mauhiz.board.impl.common.action.SelectPocketAction;
import net.mauhiz.board.impl.common.assistant.swing.button.RotatingJButton;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public abstract class PocketAwtGuiAssistant extends AwtGuiAssistant implements PocketGuiAssistant {

	protected Map<PlayerType, JPanel> pockets = new HashMap<>();

	public PocketAwtGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(PieceType pieceType, PlayerType player) {
		RotatingJButton button = new RotatingJButton();
		decorate(button, pieceType, player);
		pockets.get(player).add(button);
		button.addActionListener(new SelectPocketAction(getParent(), pieceType, player));
	}

	public void clearPockets() {
		for (JPanel pocket : pockets.values()) {
			pocket.removeAll();
		}
	}

	@Override
	protected PocketBoardGui getParent() {
		return (PocketBoardGui) super.getParent();
	}

	@Override
	public void initLayout(Dimension size) {
		super.initLayout(size);
		initPockets();
	}

	public void refreshPocketActions(PlayerType player) {
	}

	public void removeFromPocket(PieceType piece, PlayerType player) {
	}
}
