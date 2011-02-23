package net.mauhiz.board.gui.awt;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author mauhiz
 */
public class ExitAction implements ActionListener {
    private final Frame shell;

    /**
     * @param shell1
     */
    public ExitAction(Frame shell1) {
        shell = shell1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        shell.dispose();
    }
}
