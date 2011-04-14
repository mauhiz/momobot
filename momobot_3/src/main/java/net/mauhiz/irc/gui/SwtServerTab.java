package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.data.IIrcServerPeer;

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

public class SwtServerTab extends AbstractSwtTab {

    static class JoinHandler implements Listener {
        private final Text joinField;
        private final IIrcServerPeer server;
        private final SwtIrcClient swtIrcClient;

        JoinHandler(Text joinField, SwtIrcClient swtIrcClient, IIrcServerPeer server) {
            this.joinField = joinField;
            this.swtIrcClient = swtIrcClient;
            this.server = server;
        }

        @Override
        public void handleEvent(Event arg0) {
            swtIrcClient.doJoin(server, server.getNetwork().findChannel(joinField.getText()));
        }
    }

    public SwtServerTab(final SwtIrcClient swtIrcClient, final IIrcServerPeer server) {
        super(swtIrcClient);

        folder.setText(server.getNetwork().getAlias());

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
        joinButton.addListener(SWT.Selection, new JoinHandler(joinField, swtIrcClient, server));

        swtIrcClient.folderBar.addCTabFolder2Listener(new CTabFolder2Adapter() {

            @Override
            public void close(CTabFolderEvent event) {
                if (folder == event.item) {
                    swtIrcClient.doDisconnect(server);
                }
            }
        });
    }
}