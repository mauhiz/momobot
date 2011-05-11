package net.mauhiz.board.impl.shogi.gui;

import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

public interface IShogiGuiAssistant extends PocketGuiAssistant {

	void showPromotionDialog(NormalMove move);

}
