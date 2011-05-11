package net.mauhiz.board.impl.go.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import net.mauhiz.board.impl.common.gui.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.go.data.GoPlayerType;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

/**
 * @author mauhiz
 */
public class GoSwingAssistant extends PocketSwingGuiAssistant {

    public GoSwingAssistant(BoardGui parent) {
        super(parent);
    }

    @Override
    public void decorate(RotatingJButton button, Piece op) {
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getPieceType().toString());
            button.setForeground(op.getPlayerType() == GoPlayerType.BLACK ? Color.BLACK : Color.WHITE);
        }
    }

    protected GoGui getParent() {
        return (GoGui) parent;
    }

    public void initPockets() {
        JPanel pocket = newPocket();
        frame.add(pocket, 0);
        pockets.put(GoPlayerType.BLACK, pocket);
        pocket = newPocket();
        frame.add(pocket);
        pockets.put(GoPlayerType.WHITE, pocket);
    }

    private JPanel newPocket() {
        JPanel pocket = new JPanel();
        pocket.setLayout(new GridLayout(1, 7));
        pocket.setSize(7 * 30, 30);
        pocket.setToolTipText("Pocket");
        return pocket;
    }
}
