package gui;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;

/**
 * @author viper
 *
 */
public class FileExitItemListener implements SelectionListener {
    /**
     * 
     */
    public FileExitItemListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
        quit();
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        quit();
    }
    
    /**
     * 
     */
    private static void quit(){
        Display.getDefault().dispose();
    }
}
