package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class PocketSwtGuiAssistant extends SwtGuiAssistant implements PocketGuiAssistant {

	protected Map<PlayerType, Composite> pockets = new HashMap<PlayerType, Composite>();

	public PocketSwtGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(Piece piece) {
		PlayerType player = piece.getPlayerType();
		PieceType pieceType = piece.getPieceType();
		Button button = new Button(pockets.get(player), SWT.PUSH);
		button.addSelectionListener(new SelectPocketAction(getParent(), pieceType, player));
		decorate(button, piece);
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
		for (Composite pocket : pockets.values()) {
			for (Control control : pocket.getChildren()) {
				control.dispose();
			}
		}

		for (Piece piece : contents) {
			addToPocket(piece);
		}
	}
}
