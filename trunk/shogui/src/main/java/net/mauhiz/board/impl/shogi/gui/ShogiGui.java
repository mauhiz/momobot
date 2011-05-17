package net.mauhiz.board.impl.shogi.gui;

import java.awt.Color;
import java.awt.Dimension;

import net.mauhiz.board.impl.common.gui.AbstractPocketInteractiveBoardGui;
import net.mauhiz.board.impl.shogi.ShogiGameController;
import net.mauhiz.board.impl.shogi.data.ShogiBoard;
import net.mauhiz.board.impl.shogi.data.ShogiRule;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;

public class ShogiGui extends AbstractPocketInteractiveBoardGui {

	public static void main(String[] args) {
		ShogiGui gui = new ShogiGui();
		// gui.assistant = new ShogiSwtAssistant(gui);
		gui.assistant = new ShogiSwingAssistant(gui);
		gui.assistant.start();
	}

	public void afterPromotionDialog(NormalMove move, boolean promote) {
		if (promote) {
			Move promotion = getController().convertToPromotion(move);
			super.sendMove(promotion);
		} else {
			super.sendMove(move);
		}
	}

	@Override
	protected IShogiGuiAssistant getAssistant() {
		return (IShogiGuiAssistant) super.getAssistant();
	}

	@Override
	public ShogiBoard getBoard() {
		return (ShogiBoard) super.getBoard();
	}

	@Override
	public ShogiGameController getController() {
		return (ShogiGameController) super.getController();
	}

	@Override
	public Dimension getDefaultSize() {
		return new Dimension(400, 600);
	}

	@Override
	public Dimension getMinimumSize() {
		return getDefaultSize();
	}

	@Override
	public ShogiRule getRule() {
		return (ShogiRule) super.getRule();
	}

	@Override
	public Color getSquareBgcolor(Square square) {
		return Color.decode("0xFFCC66");
	}

	@Override
	public String getWindowTitle() {
		return "Shogui";
	}

	@Override
	protected GameController newController() {
		return new ShogiGameController(this);
	}

	@Override
	public synchronized void refresh() {
		super.refresh();
		getAssistant().refreshPockets(getBoard().getAllPocketPieces());
		getAssistant().refresh();
	}

	@Override
	protected void refreshSquare(Square square) {
		Piece piece = getBoard().getPieceAt(square);

		if (piece != null && piece.getPlayerType() == getTurn()) {
			addSelectAction(square);
		}

		if (selectedSquare != null) { // from the board
			// available destinations
			Move move = getRule().generateMove(selectedSquare, square, controller.getGame());

			if (move != null) {
				addMoveAction(square, move);
			}
		} else if (selectedPiece != null) { // from the pocket
			Drop drop = getRule().generateMove(selectedPiece, square, controller.getGame());
			if (drop != null) {
				addMoveAction(square, drop);
			}
		}

	}

	@Override
	public void sendMove(Move move) {
		if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
			if (getRule().canPromote(getBoard(), getSelectedSquare(), nmove.getTo())) {
				getAssistant().showPromotionDialog(nmove);
				return; // do not send move yet
			}
		}
		super.sendMove(move);
	}
}
