package net.mauhiz.board.impl.chess.gui;

import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.gui.GuiAssistant;

public interface IChessGuiAssistant extends GuiAssistant {

	void showPromotionDialog(ChessPieceType[] promotions, NormalMove nmove);
}
