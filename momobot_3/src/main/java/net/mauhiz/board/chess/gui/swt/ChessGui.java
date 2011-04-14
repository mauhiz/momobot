package net.mauhiz.board.chess.gui.swt;

import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Square;
import net.mauhiz.board.chess.gui.ChessBoardController;
import net.mauhiz.board.chess.gui.IChessGui;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessMove;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.gui.swt.AbstractSwtBoardGui;

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
public class ChessGui extends AbstractSwtBoardGui<ChessBoard, ChessMove> implements IChessGui {

    public static void main(String... args) {
        ChessGui instance = new ChessGui();

        instance.initDisplay();
        instance.swtLoop();
    }

    @Override
    public void decorate(Square square, OwnedPiece<ChessPiece, ChessPlayer> op) {
        Button button = getButton(square);
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getSymbol());
            Display display = shell.getDisplay();
            Color black = display.getSystemColor(SWT.COLOR_BLACK);
            Color white = display.getSystemColor(SWT.COLOR_WHITE);
            button.setForeground(op.getPlayer() == ChessPlayer.BLACK ? black : white);
        }
    }

    @Override
    protected String getWindowTitle() {
        return "mauhiz' Chess";
    }

    @Override
    protected ChessBoardController newController() {
        return new ChessBoardController(this);
    }

    @Override
    public void refresh() {
        shell.redraw();
    }

    public void showPromotionDialog(ChessPiece[] promotions, final ChessBoardController controller, final Square to) {
        Shell popup = new Shell(shell, SWT.ICON_QUESTION);
        popup.setLayout(new GridLayout(promotions.length, true));
        popup.setText("Promotion?");
        popup.setActive();

        for (final ChessPiece promotion : promotions) {
            Button promoButton = new Button(popup, SWT.PUSH);
            promoButton.setText(promotion.getName());
            promoButton.setToolTipText(promotion.name());
            promoButton.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent selectionevent) {
                    controller.promote(to, promotion);
                    refresh();
                }

                public void widgetSelected(SelectionEvent selectionevent) {
                    // ignored
                }
            });
        }
        popup.pack();
        popup.open();
    }
}
