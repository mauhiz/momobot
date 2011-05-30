package net.mauhiz.board.impl.common.gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Panel;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public abstract class PocketAwtGuiAssistant extends AwtGuiAssistant implements PocketGuiAssistant {

	protected Map<PlayerType, Panel> pockets = new HashMap<PlayerType, Panel>();

	public PocketAwtGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(PieceType pieceType, PlayerType player) {
		Button button = new Button(pieceType.toString());
		decorate(button, pieceType, player);
		pockets.get(player).add(button);
		button.addActionListener(new SelectPocketAction(getParent(), pieceType, player));
	}

	public void clearPockets() {
		for (Panel pocket : pockets.values()) {
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
