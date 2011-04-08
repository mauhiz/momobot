package net.mauhiz.irc.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.widgets.Composite;

public class SwtLogTab extends SwtTab {

    public SwtLogTab(final SwtIrcClient swtIrcClient) {
        super(swtIrcClient);

        folder.setText(GuiLauncher.qnet.getAlias());

        compo = new Composite(swtIrcClient.folderBar, SWT.DEFAULT);
        initReceiveBox(compo);
        folder.setControl(compo);

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