package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;
import sun.awt.VerticalBagLayout;

public abstract class PocketSwingGuiAssistant extends SwingGuiAssistant implements PocketGuiAssistant {

	protected Map<PlayerType, JPanel> pockets = new TreeMap<PlayerType, JPanel>();

	public PocketSwingGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(Piece piece) {
		PlayerType player = piece.getPlayerType();
		PieceType pieceType = piece.getPieceType();
		JButton button = new JButton(pieceType.toString());
		button.setSize(30, 30);
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
		frame.setLayout(new VerticalBagLayout());
		initPockets();
	}

	public void refreshPockets(Collection<? extends Piece> contents) {
		for (JPanel pocket : pockets.values()) {
			pocket.removeAll();
		}

		for (Piece piece : contents) {
			addToPocket(piece);
		}
	}
}
