package net.mauhiz.board.chess.gui.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;
import net.mauhiz.board.chess.model.ChessBoard;
import net.mauhiz.board.chess.model.ChessOwnedPiece;
import net.mauhiz.board.chess.model.ChessPiece;
import net.mauhiz.board.chess.model.ChessPlayer;
import net.mauhiz.board.chess.model.ChessRule;
import net.mauhiz.board.gui.awt.BoardGui;
import net.mauhiz.board.gui.awt.CancelAction;
import net.mauhiz.board.gui.awt.MoveAction;
import net.mauhiz.board.gui.awt.SelectAction;

/**
 * @author mauhiz
 */
public class ChessGui extends BoardGui {

    public static void main(String... args) {
        ChessGui instance = new ChessGui();

        instance.setSize(new Dimension(400, 400));
        instance.setMinimumSize(new Dimension(300, 300));

        /* menu */
        instance.init();
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
                final Dialog popup = new Dialog(this, "Promotion?");
                popup.setLayout(new GridLayout(1, promotions.length));
                popup.setModal(true);

                for (final ChessPiece promotion : promotions) {
                    Button promoButton = new Button(promotion.getName());
                    promoButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            getBoard().promote(to, promotion);
                            refreshBoard();
                            popup.dispose();
                        }
                    });
                    popup.add(promoButton);
                }
                popup.pack();
                popup.setVisible(true);
            }
            selectedSquare = null;
            refreshBoard();
        }
    }

    @Override
    protected synchronized void refreshBoard() {
        Color black = Color.BLACK;
        Color white = Color.WHITE;

        for (Square square : new SquareView(getBoardSize())) {
            OwnedPiece op = board.getOwnedPieceAt(square);
            Button button = buttons.get(square);
            disableButton(square);

            if (selectedSquare != null) { // from the board
                // available destinations
                ChessOwnedPiece selected = board.getOwnedPieceAt(selectedSquare);

                if (selected != null && !square.equals(selectedSquare)
                        && ChessRule.canGo(board, selected, selectedSquare, square)) {
                    enableButton(square, new MoveAction(this, square));
                }
            } else {
                // available pieces
                if (op != null && op.getPlayer() == board.getTurn()) {
                    enableButton(square, new SelectAction(this, square));
                }
            }

            if (op == null) {
                button.setLabel("");
            } else {
                button.setLabel(op.getSymbol());
                button.setForeground(op.getPlayer() == ChessPlayer.BLACK ? black : white);
            }
        }
        if (selectedSquare != null) {
            enableButton(selectedSquare, new CancelAction(this));
        }

        panel.repaint();
    }

    @Override
    public void selectPiece(Square at) {
        selectedSquare = at;
        refreshBoard();
    }
}
