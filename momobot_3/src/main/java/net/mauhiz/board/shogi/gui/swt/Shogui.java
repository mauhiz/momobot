package net.mauhiz.board.shogi.gui.swt;

import java.util.Collection;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.swt.AbstractSwtBoardGui;
import net.mauhiz.board.shogi.gui.IShogiGui;
import net.mauhiz.board.shogi.gui.ShogiBoardController;
import net.mauhiz.board.shogi.model.ShogiBoard;
import net.mauhiz.board.shogi.model.ShogiMove;
import net.mauhiz.board.shogi.model.ShogiOwnedPiece;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.board.shogi.model.ShogiPlayer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;

/**
 * @author mauhiz
 */
public class Shogui extends AbstractSwtBoardGui<ShogiBoard, ShogiMove> implements IShogiGui {

    public static void main(String... args) {
        Shogui instance = new Shogui();

        instance.initDisplay();
        instance.swtLoop();
    }

    private Composite pocket1;
    private Composite pocket2;

    protected void addToPocket(ShogiBoardController controller, ShogiPiece piece, Composite pocket) {
        Button button = new Button(pocket, SWT.PUSH);
        button.setText(piece.getName());
        button.addSelectionListener(new PocketAction(controller, piece));
    }

    @Override
    public void decorate(Square square, ShogiOwnedPiece op) {
        Button button = getButton(square);
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getSymbol());
            Color black = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);
            Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
            button.setForeground(op.getPlayer() == ShogiPlayer.BOTTOM ? black : white);
        }
    }

    @Override
    protected String getWindowTitle() {
        return "mauhiz' Shogi";
    }

    @Override
    public void initPockets() {
        pocket1 = new Group(shell, SWT.TOP);
        pocket2 = new Group(shell, SWT.BOTTOM);
    }

    @Override
    protected ShogiBoardController newController() {
        return new ShogiBoardController(this);
    }

    @Override
    public void refresh() {
        shell.redraw();
    }

    @Override
    public void refreshPocket(ShogiBoardController controller, ShogiPlayer player, Collection<ShogiPiece> content) {
        for (Control control : pocket1.getChildren()) {
            control.dispose();
        }
        for (Control control : pocket2.getChildren()) {
            control.dispose();
        }

        for (ShogiPiece piece : content) {
            addToPocket(controller, piece, player == ShogiPlayer.TOP ? pocket1 : pocket2);
        }
    }

    public void showPromotionDialog(ShogiBoardController controller, ShogiOwnedPiece piece) {
        MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        mb.setMessage("Promote?");
        int buttonID = mb.open();
        switch (buttonID) {
            case SWT.YES:
                controller.promote(piece);
                break;
            default:
                break;
        }
    }
}
