package net.mauhiz.irc.gui;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.trigger.IJoinTrigger;
import net.mauhiz.irc.base.trigger.IPartTrigger;

import org.eclipse.swt.widgets.List;

public class ChannelUpdateTrigger implements IJoinTrigger, IPartTrigger {

    private final Map<IrcChannel, List> userLists = new HashMap<IrcChannel, List>();

    public void addChannel(IrcChannel channel, List list) {
        userLists.put(channel, list);
    }

    @Override
    public void doTrigger(Join im, IIrcControl control) {
        doUpdate(im.getServer(), im.getChan());
    }

    @Override
    public void doTrigger(Part im, IIrcControl control) {
        doUpdate(im.getServer(), im.getChan());
    }

    private void doUpdate(IrcServer server, String chan) {
        final IrcChannel channel = server.findChannel(chan, false);

        if (channel != null) {
            final List userList = userLists.get(channel);

            if (userList != null) {
                userList.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        userList.removeAll();

                        for (IrcUser user : channel) {
                            userList.add(user.getNick());
                        }
                    }
                });
            }
        }
    }

    public void removeChannel(IrcChannel channel) {
        userLists.remove(channel);
    }

}
