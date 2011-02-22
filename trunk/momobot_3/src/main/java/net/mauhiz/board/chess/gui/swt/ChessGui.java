package net.mauhiz.board.chess.gui.swt;

import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.chess.model.ChessRule;
import net.mauhiz.board.gui.swt.BoardGui;
import net.mauhiz.board.gui.swt.CancelAction;
import net.mauhiz.board.gui.swt.MoveAction;
import net.mauhiz.board.gui.swt.SelectAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class ChessGui extends BoardGui {

    public static void main(String... args) {
        ChessGui instance = new ChessGui();
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

    private final ChessBoard board = new ChessBoard();
    private Square selectedSquare;

    @Override
    public void cancelSelection() {
        selectedSquare = null;
        refreshBoard();
    }

    @Override
    protected ChessBoard getBoard() {
        return board;
    }

    @Override
    public void movePiece(final Square to) {
        if (selectedSquare == null) {
            return;
        }

        if (board.move(board.getTurn(), selectedSquare, to)) {
            ChessOwnedPiece currentPiece = board.getOwnedPieceAt(to);

            if (ChessRule.canPromote(currentPiece, to)) {
                ChessPiece[] promotions = { ChessPiece.QUEEN, ChessPiece.ROOK, ChessPiece.BISHOP, ChessPiece.KNIGHT };
                Shell popup = new Shell(shell, SWT.ICON_QUESTION);
                popup.setLayout(new GridLayout(promotions.length, true));
                popup.setText("Promotion?");
                popup.setActive();

                for (final ChessPiece promotion : promotions) {
                    Button promoButton = new Button(popup, SWT.PUSH);
                    promoButton.setText(promotion.getName());
                    promoButton.setToolTipText(promotion.name());
                    promoButton.addSelectionListener(new SelectionListener() {

                        @Override
                        public void widgetDefaultSelected(SelectionEvent selectionevent) {
                            refreshBoard();
                            getBoard().promote(to, promotion);
                        }

                        @Override
                        public void widgetSelected(SelectionEvent selectionevent) {
                        }
                    });
                }
                popup.pack();
                popup.open();
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
                        && ChessRule.canGo(board, (ChessOwnedPiece) selected, selectedSquare, square)) {
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
                button.setForeground(op.getPlayer() == ChessPlayer.BLACK ? black : white);
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
