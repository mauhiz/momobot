package net.mauhiz.irc.gui;

import net.mauhiz.irc.gui.actions.SendAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class SwtTab {

    protected Composite compo;
    protected final CTabItem folder;
    protected Text receiveBox;
    protected final SwtIrcClient swtIrcClient;

    protected SwtTab(final SwtIrcClient swtIrcClient) {
        this.swtIrcClient = swtIrcClient;
        folder = new CTabItem(swtIrcClient.folderBar, SWT.CLOSE);

        swtIrcClient.folderBar.addCTabFolder2Listener(new CTabFolder2Adapter() {
            @Override
            public void restore(CTabFolderEvent ctabfolderevent) {
                swtIrcClient.display.syncExec(new Runnable() {

                    @Override
                    public void run() {
                        for (CTabItem item : swtIrcClient.folderBar.getItems()) {
                            Control control = item.getControl();
                            control.setVisible(item == folder);
                            control.redraw();
                        }
                    }
                });
            }
        });
    }

    public void appendText(String msg) {
        receiveBox.setText(receiveBox.getText() + "\r\n" + msg);
    }

    protected void initReceiveBox(Composite parent) {
        receiveBox = new Text(parent, SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        receiveBox.setText("");
        receiveBox.setEditable(false);
    }

    protected void initTypeBar(Composite parent) {
        /* Affichage de la barre de saisie */
        Label label3 = new Label(parent, SWT.DEFAULT);
        label3.setText("Input");

        Text inputBar = new Text(parent, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
        GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData2.horizontalSpan = 2;
        gridData2.grabExcessHorizontalSpace = true;
        inputBar.setLayoutData(gridData2);
        inputBar.setText("");
        inputBar.setEditable(true);

        Button send = new Button(parent, SWT.PUSH);
        send.setText("envoyer");
        send.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, GuiLauncher.qnet, "#tsi.fr"));
    }

}