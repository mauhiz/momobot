package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.data.IrcUser;

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

public class SwtLogTab extends SwtTab {

    static class ConnectHandler implements Listener {
        private final Text connectField;
        private final SwtIrcClient swtIrcClient;

        ConnectHandler(Text connectField, SwtIrcClient swtIrcClient) {
            this.connectField = connectField;
            this.swtIrcClient = swtIrcClient;
        }

        @Override
        public void handleEvent(Event event) {
            IrcServer server = IrcServerFactory.createServer(connectField.getText());
            IrcUser myself = server.newUser("momobot3");
            myself.setUser("mmb");
            myself.setFullName("momobot le 3eme");
            server.setMyself(myself);
            swtIrcClient.doConnect(server);
        }
    }

    public SwtLogTab(final SwtIrcClient swtIrcClient) {
        super(swtIrcClient);

        folder.setText("Console");

        GridLayout gridLayout = new GridLayout(1, false);
        compo.setLayout(gridLayout);
        initReceiveBox();

        Composite connectBar = new Composite(compo, SWT.BORDER);
        connectBar.setLayoutData(new GridData(SWT.FILL));
        connectBar.setLayout(new GridLayout(2, false));
        final Text connectField = new Text(connectBar, SWT.SINGLE);
        connectField.setEditable(true);
        connectField.setText("irc://irc.quakenet.org:6667/");
        setTextSize(connectField, 1, 400);

        Button joinButton = new Button(connectBar, SWT.PUSH);
        joinButton.setText("Connect");
        joinButton.addListener(SWT.Selection, new ConnectHandler(connectField, swtIrcClient));

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