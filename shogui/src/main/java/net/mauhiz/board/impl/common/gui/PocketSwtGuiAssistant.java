package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.NamedRunnable;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class PocketSwtGuiAssistant extends SwtGuiAssistant implements PocketGuiAssistant {
	private static final Logger LOG = Logger.getLogger(PocketSwtGuiAssistant.class);
	protected final Map<PlayerType, Composite> pockets = new HashMap<PlayerType, Composite>();

	public PocketSwtGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(PieceType pieceType, PlayerType player) {
		Button button = new Button(pockets.get(player), SWT.PUSH);
		button.addSelectionListener(new SelectPocketAction(getParent(), pieceType, player));
		decorate(button, pieceType, player);
	}

	@Override
	public void clear() {
		super.clear();

		new NamedRunnable("Remove Pockets") {

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			protected void trun() {
				for (Composite pocket : pockets.values()) {
					pocket.dispose();
				}
			}

		}.launch(getShell().getDisplay());
	}

	public void clearPockets() {
		new NamedRunnable("Clear Pockets") {

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			protected void trun() {
				for (Composite pocket : pockets.values()) {
					for (Control control : pocket.getChildren()) {
						control.dispose();
					}
				}
			}
		}.launch(getShell().getDisplay());

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
		for (Entry<PlayerType, Composite> pocket : pockets.entrySet()) {
			for (Control comp : pocket.getValue().getChildren()) {
				if (comp instanceof Button) {
					LOG.trace("Enabling for player: " + player + " button: " + comp);
					comp.setEnabled(player == pocket.getKey());
				}
			}
		}
	}

	public void removeFromPocket(PieceType piece, PlayerType player) {
		throw new NotImplementedException();
		//		LOG.debug("Removing from " + player + " pocket: " + piece);
		//		for (Iterator<Entry<Button, Piece>> i = pocketButtons.entrySet().iterator(); i.hasNext();) {
		//			Entry<Button, Piece> candidate = i.next();
		//			Piece inPocket = candidate.getValue();
		//			if (inPocket.getPieceType() == piece && inPocket.getPlayerType() == player) {
		//				Button button = candidate.getKey();
		//				i.remove();
		//				button.dispose();
		//				break;
		//			}
		//		}
	}
}
