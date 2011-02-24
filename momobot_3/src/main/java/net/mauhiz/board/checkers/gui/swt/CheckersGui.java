package net.mauhiz.board.checkers.gui.swt;

import net.mauhiz.board.Square;
import net.mauhiz.board.checkers.gui.CheckersBoardController;
import net.mauhiz.board.checkers.gui.ICheckersGui;
import net.mauhiz.board.checkers.model.CheckersOwnedPiece;
import net.mauhiz.board.checkers.model.CheckersPlayer;
import net.mauhiz.board.gui.BoardController;
import net.mauhiz.board.gui.swt.SwtBoardGui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public class CheckersGui extends SwtBoardGui implements ICheckersGui {
    public static void main(String... args) {
        CheckersGui instance = new CheckersGui();

        instance.initDisplay();
        instance.swtLoop();
    }

    @Override
    public void decorate(Square square, CheckersOwnedPiece op) {
        Button button = getButton(square);
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getSymbol());

            Display display = shell.getDisplay();
            Color black = display.getSystemColor(SWT.COLOR_BLACK);
            Color white = display.getSystemColor(SWT.COLOR_WHITE);
            button.setForeground(op.getPlayer() == CheckersPlayer.BLACK ? black : white);
        }
    }

    @Override
    protected String getWindowTitle() {
        return "mauhiz' Checkers";
    }

    @Override
    protected BoardController newController() {
        return new CheckersBoardController(this);
    }

    @Override
    public void refresh() {
        shell.redraw();
    }
}
