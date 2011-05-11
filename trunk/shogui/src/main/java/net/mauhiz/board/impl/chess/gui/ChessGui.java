package net.mauhiz.board.impl.chess.gui;

import java.awt.Color;

import net.mauhiz.board.impl.chess.ChessGameController;
import net.mauhiz.board.impl.chess.data.ChessBoard;
import net.mauhiz.board.impl.chess.data.ChessPiece;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessRule;
import net.mauhiz.board.impl.common.gui.AbstractInteractiveBoardGui;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Square;

/**
 * @author mauhiz
 */
public class ChessGui extends AbstractInteractiveBoardGui {

	public static void main(String... args) {
		ChessGui gui = new ChessGui();
		gui.assistant = new ChessGuiAssistant(gui);
		gui.assistant.start();
	}

	@Override
	public String getWindowTitle() {
		return "mauhiz' Chess";
	}

	@Override
	protected ChessGameController newController() {
		return new ChessGameController(this);
	}
	
	@Override
	protected ChessBoard getBoard() {
		return (ChessBoard) super.getBoard();
	}
	
	@Override
	public ChessRule getRule() {
		return (ChessRule) super.getRule();
	}

	@Override
	protected void refreshSquare(Square square) {
		ChessPiece op = getBoard().getPieceAt(square);
		disableSquare(square);

		if (selectedSquare == null) {// available pieces
			if (op != null && op.getPlayerType() == getTurn()) {
				addSelectAction(square);
			}
		} else { // from the board
			// available destinations
			ChessPiece selected = getBoard().getPieceAt(selectedSquare);

			if (selected != null && !square.equals(selectedSquare)
					&& getRule().canGo(getBoard(), selectedSquare, square)) {
				addMoveAction(square);
			}
		}
	}

	@Override
	public Color getSquareBgcolor(Square square) {
		return (square.getX() + square.getY()) % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY;
	}
	
	@Override
	public void sendMove(Move move) {
		if (move instanceof NormalMove) {
			NormalMove nmove = (NormalMove) move;
	
			if (getRule().canPromote(getBoard().getPieceAt(nmove.getFrom()), nmove.getTo())) {
				ChessPieceType[] promotions = { ChessPieceType.QUEEN, ChessPieceType.ROOK, ChessPieceType.BISHOP, ChessPieceType.KNIGHT };
				getAssistant().showPromotionDialog(promotions, nmove);
			}
			
			return; // do not send anything yet
		}

		selectedSquare = null;
		refresh();
		super.sendMove(move);
	}

	private ChessGuiAssistant getAssistant() {
		return (ChessGuiAssistant) assistant;
	}
}
