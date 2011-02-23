package net.mauhiz.board.chess.gui.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.Square;
import net.mauhiz.board.chess.gui.ChessBoardController;
import net.mauhiz.board.chess.gui.IChessGui;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.gui.BoardController;
import net.mauhiz.board.gui.awt.BoardGui;

/**
 * @author mauhiz
 */
public class ChessGui extends BoardGui implements IChessGui {

    public static void main(String... args) {
        ChessGui instance = new ChessGui();

        instance.setSize(new Dimension(400, 400));
        instance.setMinimumSize(new Dimension(300, 300));

        instance.initDisplay();
    }

    public void decorate(Square square, ChessOwnedPiece op) {

        Button button = buttons.get(square);
        if (op == null) {
            button.setLabel("");
        } else {
            button.setLabel(op.getSymbol());
            button.setForeground(op.getPlayer() == ChessPlayer.BLACK ? Color.BLACK : Color.WHITE);
        }
    }

    @Override
    protected BoardController newController() {
        return new ChessBoardController(this);
    }

    @Override
    public void refresh() {
        panel.repaint();
    }

    @Override
    public void showPromotionDialog(ChessPiece[] promotions, final ChessBoardController controller, final Square to) {
        final Dialog popup = new Dialog(this, "Promotion?");
        popup.setLayout(new GridLayout(1, promotions.length));
        popup.setModal(true);

        for (final ChessPiece promotion : promotions) {
            Button promoButton = new Button(promotion.getName());
            promoButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.promote(to, promotion);
                    popup.dispose();
                    refresh();
                }
            });
            popup.add(promoButton);
        }
        popup.pack();
        popup.setVisible(true);
    }
}
