package net.mauhiz.irc.gui;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.irc.base.data.ArgumentList;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Topic;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.SetTopic;
import net.mauhiz.irc.base.msg.Whois;
import net.mauhiz.irc.gui.actions.SendAction;
import net.mauhiz.irc.gui.actions.SendMeAction;
import net.mauhiz.irc.gui.actions.SendNoticeAction;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

public class SwtChanTab extends AbstractSwtTab {

    abstract class AbstractUserMenuAction extends AbstractAction {
        protected List<IrcUser> findUsers() {
            List<IrcUser> users = new ArrayList<IrcUser>();

            String[] selectedNicks = usersInChan.getSelection();
            for (String selectedNick : selectedNicks) {
                users.add(server.getNetwork().findUser(selectedNick, false));
            }
            return users;
        }

        @Override
        protected ExecutionType getExecutionType() {
            return ExecutionType.GUI_SYNCHRONOUS;
        }
    }

    class BanAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                String modeStr = "+b " + user.getMask(); // TODO make this higher level
                Mode ban = new Mode(server, null, channel, new ArgumentList(modeStr));
                swtIrcClient.gtm.client.sendMsg(ban);
            }
        }
    }

    class CloseHandler extends CTabFolder2Adapter {

        @Override
        public void close(CTabFolderEvent event) {
            if (event.item == folder && channel.contains(server.getMyself())) {
                Part msg = new Part(server, "@+", channel);
                swtIrcClient.gtm.client.sendMsg(msg);
                swtIrcClient.cut.removeChannel(channel);
                swtIrcClient.chanTabs.remove(channel);
                Logger.getLogger(SwtChanTab.class).info("Closed channel tab: " + channel);
            }
        }
    }

    class KickAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                Kick kick = new Kick(server, null, channel, user, null);
                swtIrcClient.gtm.client.sendMsg(kick);
            }
        }
    }

    class OpAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                String modeStr = "+o " + user; // TODO make this higher level
                Mode op = new Mode(server, null, channel, new ArgumentList(modeStr));
                swtIrcClient.gtm.client.sendMsg(op);
            }
        }
    }

    class QueryAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                swtIrcClient.createPrivateTab(server, user);
            }
        }
    }

    class SetTopicAction extends AbstractAction {

        @Override
        protected void doAction() {
            if (topicBar.getEditable()) {
                SetTopic setTopic = new SetTopic(server, null, channel, topicBar.getText());
                swtIrcClient.gtm.client.sendMsg(setTopic);
            }
        }

        @Override
        protected ExecutionType getExecutionType() {
            return ExecutionType.GUI_ASYNCHRONOUS;
        }
    }

    class ToggleModerateAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            String modeStr = channel.getProperties().isModerated() ? "-m" : "+m"; // TODO make this higher level
            Mode mute = new Mode(server, null, channel, new ArgumentList(modeStr));
            swtIrcClient.gtm.client.sendMsg(mute);
        }

    }

    class VoiceAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                String modeStr = "+v " + user; // TODO make this higher level
                Mode voice = new Mode(server, null, channel, new ArgumentList(modeStr));
                swtIrcClient.gtm.client.sendMsg(voice);
            }
        }
    }

    class WhoisAction extends AbstractUserMenuAction {

        @Override
        protected void doAction() {
            String[] selectedNicks = usersInChan.getSelection();
            Whois whois = new Whois(server, null, selectedNicks);
            swtIrcClient.gtm.client.sendMsg(whois);
        }
    }

    final IrcChannel channel;
    final IIrcServerPeer server;
    final Text topicBar;
    final org.eclipse.swt.widgets.List usersInChan;

    public SwtChanTab(final SwtIrcClient swtIrcClient, final IIrcServerPeer server, final IrcChannel channel) {
        super(swtIrcClient);
        this.server = server;
        this.channel = channel;

        Composite titleBar = new Composite(compo, SWT.BORDER);
        titleBar.setLayout(new GridLayout(2, false));

        // topic bar
        topicBar = new Text(titleBar, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);

        // control buttons (chan flags...)
        Composite controlGroup = new Composite(titleBar, SWT.BORDER | SWT.FILL);
        controlGroup.setLayout(new GridLayout(4, false));
        controlGroup.setBackground(controlGroup.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        Button setTopic = new Button(controlGroup, SWT.PUSH);
        setTopic.setText("Set topic");
        setTopic.addSelectionListener(new SetTopicAction());

        Button setModerate = new Button(controlGroup, SWT.PUSH);
        setModerate.setText("Toggle mod.");
        setModerate.addSelectionListener(new ToggleModerateAction());

        Composite mainPanel = new Composite(compo, SWT.BORDER);
        mainPanel.setLayout(new GridLayout(2, false));
        initReceiveBox(mainPanel);
        usersInChan = new org.eclipse.swt.widgets.List(mainPanel, SWT.BORDER | SWT.V_SCROLL);
        setListSize(usersInChan, 15, 150);
        initUserActionsMenu();
        initTypeBar();

        // leave channel on close tab
        swtIrcClient.folderBar.addCTabFolder2Listener(new CloseHandler());

        folder.setText(getFolderName());
    }

    @Override
    protected final String getFolderName() {
        return channel.fullName();
    }

    public org.eclipse.swt.widgets.List getUsersInChan() {
        return usersInChan;
    }

    protected final void initTypeBar() {
        Composite typeBar = new Composite(compo, SWT.BORDER);
        typeBar.setLayout(new GridLayout(4, false));

        /* Affichage de la barre de saisie */
        final Text inputBar = new Text(typeBar, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
        inputBar.setText(StringUtils.EMPTY);
        inputBar.setEditable(true);
        setTextSize(inputBar, 1, 400);
        inputBar.setBackground(inputBar.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));

        Button sendText = new Button(typeBar, SWT.PUSH);
        sendText.setText("Send text");
        sendText.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, server, channel));

        inputBar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == '\r') {
                    new SendAction(inputBar, swtIrcClient.gtm, server, channel).doSwtAction(arg0);
                }
            }
        });

        Button sendNotice = new Button(typeBar, SWT.PUSH);
        sendNotice.setText("Send notice");
        sendNotice.addSelectionListener(new SendNoticeAction(inputBar, swtIrcClient.gtm, server, channel));

        Button sendAction = new Button(typeBar, SWT.PUSH);
        sendAction.setText("Send action");
        sendAction.addSelectionListener(new SendMeAction(inputBar, swtIrcClient.gtm, server, channel));
    }

    private void initUserActionsMenu() {
        Menu userActions = new Menu(usersInChan);
        MenuItem query = new MenuItem(userActions, SWT.DEFAULT);
        query.setText("&Query");
        query.addSelectionListener(new QueryAction());

        MenuItem kick = new MenuItem(userActions, SWT.DEFAULT);
        kick.setText("&Kick");
        kick.addSelectionListener(new KickAction());

        MenuItem ban = new MenuItem(userActions, SWT.DEFAULT);
        ban.setText("&Ban");
        ban.addSelectionListener(new BanAction());

        MenuItem op = new MenuItem(userActions, SWT.DEFAULT);
        op.setText("&Op");
        op.addSelectionListener(new OpAction());

        MenuItem voice = new MenuItem(userActions, SWT.DEFAULT);
        voice.setText("&Voice");
        voice.addSelectionListener(new VoiceAction());

        MenuItem whois = new MenuItem(userActions, SWT.DEFAULT);
        whois.setText("&Whois");
        whois.addSelectionListener(new WhoisAction());

        usersInChan.setMenu(userActions);
    }

    public void setTopicEditable(boolean editable) {
        topicBar.setEditable(editable);
    }

    public void updateTopic(Topic topic) {
        topicBar.setText(topic.toString());
    }
}