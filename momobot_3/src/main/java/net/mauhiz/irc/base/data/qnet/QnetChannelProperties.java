package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.ChannelProperties;

/**
 * @author mauhiz
 */
class QnetChannelProperties extends ChannelProperties {
    // Hidden users present (+d) 
    private boolean hiddenUsers;

    public boolean isHiddenUsers() {
        return hiddenUsers;
    }

    @Override
    public void process(boolean set, char mode, String... args) {
        if (mode == 'd') {
            hiddenUsers = set;
        } else {
            super.process(set, mode, args);
        }
    }

    public void setHiddenUsers(boolean hiddenUsers) {
        this.hiddenUsers = hiddenUsers;
    }

}
