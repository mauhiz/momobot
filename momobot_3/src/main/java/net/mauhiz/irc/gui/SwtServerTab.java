package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.data.IrcServer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class SwtServerTab extends SwtTab {

    public SwtServerTab(final SwtIrcClient swtIrcClient, final IrcServer server) {
        super(swtIrcClient);

        folder.setText(server.getAlias());

        GridLayout gridLayout = new GridLayout(1, false);
        compo.setLayout(gridLayout);
        initReceiveBox();

        Composite joinBar = new Composite(compo, SWT.BORDER);
        joinBar.setLayoutData(new GridData(SWT.FILL));
        joinBar.setLayout(new GridLayout(2, false));
        final Text joinField = new Text(joinBar, SWT.SINGLE);
        joinField.setEditable(true);
        Button joinButton = new Button(joinBar, SWT.PUSH);
        joinButton.setText("Join");
        joinButton.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event arg0) {
                swtIrcClient.doJoin(server, joinField.getText());
            }
        });

        swtIrcClient.folderBar.addCTabFolder2Listener(new CTabFolder2Adapter() {

            @Override
            public void close(CTabFolderEvent event) {
                if (folder == event.item) {
                    event.doit = false;
                }
            }
        });
    }
}