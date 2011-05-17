package net.mauhiz.board.impl.common.gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Panel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public abstract class PocketAwtGuiAssistant extends AwtGuiAssistant implements PocketGuiAssistant {

	protected Map<PlayerType, Panel> pockets = new HashMap<PlayerType, Panel>();

	public PocketAwtGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(Piece piece) {
		PlayerType player = piece.getPlayerType();
		PieceType pieceType = piece.getPieceType();
		Button button = new Button(pieceType.toString());
		pockets.get(player).add(button);
		button.addActionListener(new SelectPocketAction(getParent(), pieceType, player));
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

	public void refreshPockets(Collection<? extends Piece> contents) {
		for (Panel pocket : pockets.values()) {
			pocket.removeAll();
		}

		for (Piece piece : contents) {
			addToPocket(piece);
		}
	}
}
