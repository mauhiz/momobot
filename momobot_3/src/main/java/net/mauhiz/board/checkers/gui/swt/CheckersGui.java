package net.mauhiz.board.checkers.gui.swt;

import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;
import net.mauhiz.board.checkers.model.CheckersBoard;
import net.mauhiz.board.checkers.model.CheckersOwnedPiece;
import net.mauhiz.board.checkers.model.CheckersPlayer;
import net.mauhiz.board.checkers.model.CheckersRule;
import net.mauhiz.board.gui.swt.BoardGui;
import net.mauhiz.board.gui.swt.CancelAction;
import net.mauhiz.board.gui.swt.MoveAction;
import net.mauhiz.board.gui.swt.SelectAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class CheckersGui extends BoardGui {

    public static void main(String... args) {
        CheckersGui instance = new CheckersGui();
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

    private final CheckersBoard board = new CheckersBoard();
    private Square selectedSquare;

    @Override
    public void cancelSelection() {
        selectedSquare = null;
        refreshBoard();
    }

    @Override
    protected CheckersBoard getBoard() {
        return board;
    }

    @Override
    public void movePiece(final Square to) {
        if (selectedSquare == null) {
            return;
        }

        if (board.move(board.getTurn(), selectedSquare, to)) {
            CheckersOwnedPiece currentPiece = board.getOwnedPieceAt(to);

            if (CheckersRule.canPromote(currentPiece, to)) {
                currentPiece.setPromoted(true);
            }
            selectedSquare = null;
            refreshBoard();
        }
    }

    @Override
    protected void refreshBoard() {
        Display display = shell.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        shell.setRedraw(false);

        for (Square square : new SquareView(getBoardSize())) {
            OwnedPiece op = board.getOwnedPieceAt(square);
            Button button = buttons.get(square);
            disableButton(square);

            if (selectedSquare != null) { // from the board
                // available destinations
                OwnedPiece selected = board.getOwnedPieceAt(selectedSquare);

                if (!square.equals(selectedSquare)
                        && CheckersRule.canGo(board, (CheckersOwnedPiece) selected, selectedSquare, square)) {
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
                button.setForeground(op.getPlayer() == CheckersPlayer.BLACK ? black : white);
            }
        }
        if (selectedSquare != null) {
            enableButton(selectedSquare, new CancelAction(this));
        }

        shell.setRedraw(true);
    }

    @Override
    public void selectPiece(Square at) {
        selectedSquare = at;
        refreshBoard();
    }
}
