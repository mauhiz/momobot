package net.mauhiz.irc.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.trigger.IJoinTrigger;
import net.mauhiz.irc.base.trigger.IKickTrigger;
import net.mauhiz.irc.base.trigger.IPartTrigger;
import net.mauhiz.irc.base.trigger.IQuitTrigger;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.NamedRunnable;

public class ChannelUpdateTrigger implements IJoinTrigger, IPartTrigger, IQuitTrigger, IKickTrigger {

    static class UserListUpdater extends NamedRunnable {
        private final IrcChannel channel;
        private final org.eclipse.swt.widgets.List userList;

        public UserListUpdater(org.eclipse.swt.widgets.List userList, IrcChannel channel) {
            super("User List Updater");
            this.userList = userList;
            this.channel = channel;
        }

        @Override
        protected ExecutionType getExecutionType() {
            return ExecutionType.GUI_ASYNCHRONOUS;
        }

        @Override
        public void trun() {
            userList.removeAll();

            for (IrcUser user : channel) {
                userList.add(user.getNick());
            }
        }
    }

    private final Map<IrcChannel, org.eclipse.swt.widgets.List> userLists = new HashMap<IrcChannel, org.eclipse.swt.widgets.List>();

    public void addChannel(IrcChannel channel, org.eclipse.swt.widgets.List list) {
        userLists.put(channel, list);
    }

    public void doTrigger(Join im, IIrcControl control) {
        doUpdate(im.getChans());
    }

    public void doTrigger(Kick im, IIrcControl control) {
        doUpdate(im.getChans());
    }

    public void doTrigger(Part im, IIrcControl control) {
        doUpdate(im.getChans());
    }

    public void doTrigger(Quit im, IIrcControl control) {
        doUpdate((IrcChannel) null);
    }

    public void doUpdate(IrcChannel... channels) {
        Collection<IrcChannel> chans = channels == null || channels.length == 0 ? userLists.keySet() : Arrays
                .asList(channels);
        for (IrcChannel channel : chans) {
            org.eclipse.swt.widgets.List userList = userLists.get(channel);

            if (userList != null) {
                new UserListUpdater(userList, channel).launch(userList.getDisplay());
            }
        }
    }

    public void removeChannel(IrcChannel channel) {
        userLists.remove(channel);
    }

}
