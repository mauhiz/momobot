package net.mauhiz.board.shogi.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.BoardController;
import net.mauhiz.board.gui.swing.SwingBoardGui;
import net.mauhiz.board.shogi.gui.IShogiGui;
import net.mauhiz.board.shogi.gui.ShogiBoardController;
import net.mauhiz.board.shogi.gui.awt.PocketAction;
import net.mauhiz.board.shogi.model.ShogiOwnedPiece;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.board.shogi.model.ShogiPlayer;

/**
 * @author mauhiz
 */
public class SwingShogui extends SwingBoardGui implements IShogiGui {

    public static void main(String... args) {
        SwingShogui instance = new SwingShogui();

        instance.initDisplay();
    }

    private JPanel pocket1;

    private JPanel pocket2;

    protected void addToPocket(ShogiBoardController controller, ShogiPiece piece, JPanel pocket) {
        JButton button = new JButton();
        button.setText(piece.getName());
        button.addActionListener(new PocketAction(controller, piece));
        pocket.add(button);
    }

    public void decorate(Square square, ShogiOwnedPiece op) {

        JButton button = getButton(square);
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getSymbol());
            button.setForeground(op.getPlayer() == ShogiPlayer.BOTTOM ? Color.BLACK : Color.WHITE);
        }
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(500, 500);
    }

    public void initPockets() {
        pocket1 = new JPanel();
        frame.add(pocket1, 0);

        pocket2 = new JPanel();
        frame.add(pocket2);
    }

    @Override
    protected BoardController newController() {
        return new ShogiBoardController(this);
    }

    @Override
    public void refresh() {
        frame.repaint();
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
        final JDialog popup = new JDialog(frame, "Promotion?");
        popup.setLayout(new GridLayout(1, 2, 10, 10));
        popup.setModal(true);

        JButton promoButton = new JButton("Yes");
        promoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.promote(currentPiece);
                popup.dispose();
                refresh();
            }
        });
        popup.add(promoButton);

        JButton cancelButton = new JButton("No");
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
