package net.mauhiz.board.shogi.gui;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Map.Entry;

import net.mauhiz.board.MoveReader;
import net.mauhiz.board.Square;
import net.mauhiz.board.gui.AbstractGuiBoardController;
import net.mauhiz.board.shogi.model.Drop;
import net.mauhiz.board.shogi.model.ShogiBoard;
import net.mauhiz.board.shogi.model.ShogiMove;
import net.mauhiz.board.shogi.model.ShogiOwnedPiece;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.board.shogi.model.ShogiPlayer;
import net.mauhiz.board.shogi.model.ShogiRule;

import org.apache.commons.lang.NotImplementedException;

public class ShogiBoardController extends AbstractGuiBoardController<ShogiBoard, ShogiMove> {

    private ShogiPiece selectedPiece;

    public ShogiBoardController(IShogiGui display) {
        super(display, new ShogiBoard());
        getBoard().setRule(new ShogiRule());
    }

    @Override
    protected IShogiGui getDisplay() {
        return super.getDisplay();
    }

    @Override
    public void init() {
        clear();

        getBoard().newGame();
        final Dimension size = getBoardSize();

        getDisplay().initLayout(size);

        getDisplay().initPockets();

        for (Square square : getSquares()) {
            getDisplay().appendSquare(square, size);
        }

        refresh();
    }

    @Override
    public void movePiece(final Square to) {
        if (selectedSquare == null) {
            if (selectedPiece != null) {
                Drop drop = new Drop();
                drop.setTo(to);
                drop.dropped = selectedPiece;

                if (getBoard().move(drop)) {
                    selectedPiece = null;
                    refresh();
                }
            }
        } else {
            ShogiMove move = new ShogiMove();
            move.setFrom(selectedSquare);
            move.setTo(to);

            if (getBoard().move(move)) {

                if (ShogiRule.canPromote(getBoard(), selectedSquare, to)) {
                    getDisplay().showPromotionDialog(this, getBoard().getOwnedPieceAt(to));
                }

                selectedSquare = null;
                refresh();
            }
        }
    }

    @Override
    public MoveReader<ShogiBoard, ShogiMove> newMoveReader() {
        throw new NotImplementedException();
    }

    public void promote(ShogiOwnedPiece to) {
        to.setPromoted(true);
        refresh();
    }

    @Override
    public synchronized void refresh() {
        for (Square square : getSquares()) {
            ShogiOwnedPiece op = getBoard().getOwnedPieceAt(square);
            getDisplay().disableSquare(square);

            if (selectedSquare == null) {
                if (selectedPiece == null) { // available pieces
                    if (op != null && op.getPlayer() == getBoard().getTurn()) {
                        getDisplay().addSelectAction(square, this);
                    }
                } else { // from the pocket
                    if (ShogiRule.canDrop(getBoard(), square)) {
                        getDisplay().addMoveAction(square, this);
                    }
                }
            } else { // from the board
                // available destinations
                ShogiOwnedPiece selected = getBoard().getOwnedPieceAt(selectedSquare);

                if (selected != null && !square.equals(selectedSquare)
                        && ShogiRule.canGo(getBoard(), selectedSquare, square)) {
                    getDisplay().addMoveAction(square, this);
                }
            }

            getDisplay().decorate(square, op);

        }
        if (selectedSquare != null) {
            getDisplay().addCancelAction(selectedSquare, this);
        }
        refreshPockets();
        getDisplay().refresh();
    }

    private void refreshPockets() {
        for (Entry<ShogiPlayer, Collection<ShogiPiece>> pocket : getBoard().getPockets()) {
            getDisplay().refreshPocket(this, pocket.getKey(), pocket.getValue());
        }
    }

    public void selectPiece(ShogiPiece inPocket) {
        selectedPiece = inPocket;
        refresh();
    }
}
