package net.mauhiz.irc.gui.actions;

import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class ExitAction extends AbstractAction {
    private final Shell shell;

    /**
     * @param shell1
     */
    public ExitAction(Shell shell1) {
        super();
        shell = shell1;
    }

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.GUI_SYNCHRONOUS; // I want to quit now!
    }

    @Override
    public void trun() {
        shell.close();
        Display.getDefault().dispose();
    }
}
