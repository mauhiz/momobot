package net.mauhiz.board.chess.gui.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import net.mauhiz.board.Square;
import net.mauhiz.board.chess.gui.ChessBoardController;
import net.mauhiz.board.chess.gui.IChessGui;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessMove;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.gui.swing.SwingBoardGui;

/**
 * @author mauhiz
 */
public class SwingChessGui extends SwingBoardGui<ChessBoard, ChessMove> implements IChessGui {

    public static void main(String... args) {
        SwingChessGui instance = new SwingChessGui();

        instance.initDisplay();
    }

    public void decorate(Square square, ChessOwnedPiece op) {

        JButton button = getButton(square);
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getSymbol());
            button.setForeground(op.getPlayer() == ChessPlayer.BLACK ? Color.BLACK : Color.WHITE);
        }
    }

    @Override
    protected ChessBoardController newController() {
        return new ChessBoardController(this);
    }

    @Override
    public void refresh() {
        panel.repaint();
    }

    @Override
    public void showPromotionDialog(ChessPiece[] promotions, final ChessBoardController controller, final Square to) {
        final JDialog popup = new JDialog(frame, "Promotion?");
        popup.setLayout(new GridLayout(1, promotions.length));
        popup.setModal(true);

        for (final ChessPiece promotion : promotions) {
            JButton promoButton = new JButton(promotion.getName());
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
