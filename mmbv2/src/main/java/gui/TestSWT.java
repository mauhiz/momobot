package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class TestSWT {
    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(SWT.getPlatform());
        System.out.println(SWT.getVersion());
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setSize(200, 200);
        //final Cursor[] cursor = new Cursor[1];
        shell.setText("momoIRC");
        Button button = new Button(shell, SWT.PUSH);
        button.setText("Change cursor");
        Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        button.setSize(size);
        setUpMenu(shell);
        shell.open();
        /* Set up the event loop. */
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                /* If no more entries in the event queue */
                display.sleep();
            }
        }
        display.dispose();
    }
    
    /**
     * @param shell
     */
    private static void setUpMenu(Shell shell){
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);

        MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
        fileSaveItem.setText("&Save");

        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("E&xit");

        MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        helpMenuHeader.setText("&Help");

        Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
        helpMenuHeader.setMenu(helpMenu);

        MenuItem helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);
        helpGetHelpItem.setText("&Get Help");

        fileExitItem.addSelectionListener(new FileExitItemListener());
        fileSaveItem.addSelectionListener(new FileSaveItemListener());
        helpGetHelpItem.addSelectionListener(new HelpGetHelpItemListener());

        shell.setMenuBar(menuBar);
    }
}
