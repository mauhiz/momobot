package net.mauhiz.board.impl.chess.gui;

import net.mauhiz.board.impl.chess.PromoteMove;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

public class PromoteAction extends AbstractAction {
    private final NormalMove nmove;
    private final BoardGui lparent;
    private final ChessPieceType promotion;

    public PromoteAction(NormalMove nmove, BoardGui lparent, ChessPieceType promotion) {
        this.nmove = nmove;
        this.lparent = lparent;
        this.promotion = promotion;
    }

    @Override
    protected void doAction() {
        Move promoteMove = new PromoteMove(nmove, promotion);
        lparent.sendMove(promoteMove);
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.NON_GUI;
    }
}