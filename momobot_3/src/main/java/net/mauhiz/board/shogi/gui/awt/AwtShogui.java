package net.mauhiz.board.shogi.gui.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.awt.AbstractAwtBoardGui;
import net.mauhiz.board.shogi.gui.IShogiGui;
import net.mauhiz.board.shogi.gui.ShogiBoardController;
import net.mauhiz.board.shogi.model.ShogiBoard;
import net.mauhiz.board.shogi.model.ShogiMove;
import net.mauhiz.board.shogi.model.ShogiOwnedPiece;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.board.shogi.model.ShogiPlayer;

/**
 * @author mauhiz
 */
public class AwtShogui extends AbstractAwtBoardGui<ShogiBoard, ShogiMove> implements IShogiGui {

    public static void main(String... args) {
        AwtShogui instance = new AwtShogui();

        instance.initDisplay();
    }

    private final Panel pocket1 = new Panel();
    private final Panel pocket2 = new Panel();

    protected void addToPocket(ShogiBoardController controller, ShogiPiece piece, Panel pocket) {
        Button button = new Button(piece.getName());
        button.addActionListener(new PocketAction(controller, piece));
        pocket.add(button);
    }

    public void decorate(Square square, ShogiOwnedPiece op) {

        Button button = getButton(square);
        if (op == null) {
            button.setLabel("");
        } else {
            button.setLabel(op.getSymbol());
            button.setForeground(op.getPlayer() == ShogiPlayer.BOTTOM ? Color.BLACK : Color.WHITE);
        }
    }

    @Override
    public void initPockets() {
        frame.add(pocket1, 0);
        frame.add(pocket2);
    }

    @Override
    protected ShogiBoardController newController() {
        return new ShogiBoardController(this);
    }

    @Override
    public void refresh() {
        panel.repaint();
    }

    @Override
    public void refreshPocket(ShogiBoardController controller, ShogiPlayer player, Collection<ShogiPiece> content) {
        pocket1.removeAll();
        pocket2.removeAll();
        for (ShogiPiece piece : content) {
            addToPocket(controller, piece, player == ShogiPlayer.TOP ? pocket1 : pocket2);
        }
    }

    @Override
    public void showPromotionDialog(final ShogiBoardController controller, final ShogiOwnedPiece currentPiece) {
        final Dialog popup = new Dialog(frame, "Promotion?");
        popup.setLayout(new GridLayout(1, 2, 10, 10));
        popup.setModal(true);

        Button promoButton = new Button("Yes");
        promoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.promote(currentPiece);
                popup.dispose();
                refresh();
            }
        });
        popup.add(promoButton);

        Button cancelButton = new Button("No");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                popup.dispose();
                refresh();
            }
        });
        popup.add(cancelButton);

        popup.pack();
        popup.setVisible(true);
    }
}
