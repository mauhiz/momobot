package net.mauhiz.board.shogi.gui.swt;

import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;
import net.mauhiz.board.gui.swt.BoardGui;
import net.mauhiz.board.gui.swt.CancelAction;
import net.mauhiz.board.gui.swt.MoveAction;
import net.mauhiz.board.gui.swt.SelectAction;
import net.mauhiz.board.shogi.model.ShogiBoard;
import net.mauhiz.board.shogi.model.ShogiOwnedPiece;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.board.shogi.model.ShogiPlayer;
import net.mauhiz.board.shogi.model.ShogiRule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class Shogui extends BoardGui {

    public static void main(String... args) {
        Shogui instance = new Shogui();
        Display display = new Display();
        Shell shell = new Shell(display);

        shell.setSize(400, 400);
        shell.setMinimumSize(400, 400);

        /* menu */

        instance.shell = shell;
        instance.initMenu();

        shell.open();
        shell.pack();
        // SWT loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private final ShogiBoard board = new ShogiBoard();
    private ShogiPiece selectedPiece;
    private Square selectedSquare;

    @Override
    public void cancelSelection() {
        selectedSquare = null;
        selectedPiece = null;
        refreshBoard();
    }

    private void displayPockets() {
        // TODO pocket bar for W
        // TODO pocket bar for b
        // TODO select actions on pocket
    }

    @Override
    protected ShogiBoard getBoard() {
        return board;
    }

    @Override
    public void movePiece(Square to) {
        if (selectedSquare != null) {
            if (board.move(board.getTurn(), selectedSquare, to)) {
                ShogiOwnedPiece currentPiece = board.getOwnedPieceAt(to);

                if (ShogiRule.canPromote(currentPiece, selectedSquare, to)) {
                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    mb.setMessage("Promote?");
                    int buttonID = mb.open();
                    switch (buttonID) {
                        case SWT.YES:
                            currentPiece.setPromoted(true);
                            break;
                        default:
                            break;
                    }
                }
                selectedSquare = null;
                refreshBoard();
            }
        } else if (selectedPiece != null) {
            if (board.drop(board.getTurn(), selectedPiece, to)) {
                selectedPiece = null;
                refreshBoard();
            }
        }
    }

    @Override
    protected void refreshBoard() {
        Display display = shell.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        shell.setRedraw(false);

        for (Square square : new SquareView(getBoardSize())) {
            ShogiOwnedPiece op = board.getOwnedPieceAt(square);
            Button button = buttons.get(square);
            disableButton(square);

            if (selectedSquare != null) { // from the board
                // available destinations
                ShogiOwnedPiece selected = board.getOwnedPieceAt(selectedSquare);

                if (!square.equals(selectedSquare) && ShogiRule.canGo(board, selected, selectedSquare, square)) {
                    enableButton(square, new MoveAction(this, square));
                }
            } else if (selectedPiece != null) { // from the pocket
                if (ShogiRule.canDrop(board, square)) {
                    enableButton(square, new MoveAction(this, square));
                }
            } else {
                // available pieces
                if (op != null && op.getPlayer() == board.getTurn()) {
                    enableButton(square, new SelectAction(this, square));
                }
            }

            if (op == null) {
                button.setText("");
            } else {
                button.setText(op.getSymbol());
                button.setForeground(op.getPlayer() == ShogiPlayer.BOTTOM ? black : white);
            }
        }
        if (selectedSquare != null) {
            enableButton(selectedSquare, new CancelAction(this));
        }

        displayPockets();
        shell.setRedraw(true);
    }

    public void selectPiece(ShogiPiece inPocket) {
        selectedPiece = inPocket;
        refreshBoard();
    }

    @Override
    public void selectPiece(Square at) {
        selectedSquare = at;
        refreshBoard();
    }
}
