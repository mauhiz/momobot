package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public abstract class PocketSwingGuiAssistant extends SwingGuiAssistant implements PocketGuiAssistant {

	protected Map<PlayerType, JPanel> pockets = new HashMap<PlayerType, JPanel>();
	
	public PocketSwingGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void addToPocket(Piece piece) {
		PlayerType player = piece.getPlayerType();
		PieceType pieceType = piece.getPieceType();
		JButton button = new JButton(pieceType.toString());
		pockets.get(player).add(button);
		button.addActionListener(new SelectPocketAction((PocketBoardGui) parent, pieceType, player));
	}

	@Override
	public void initLayout(Dimension size) {
		super.initLayout(size);
		frame.setLayout(new GridLayout(3, 1, 5, 0));
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
