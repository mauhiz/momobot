package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.ChannelProperties;

/**
 * @author mauhiz
 */
class QnetChannelProperties extends ChannelProperties {
    // Hidden users present (+d) 
    private boolean hiddenUsers;

    // No control codes (+c)
    private boolean noControls;

    // Prohibits channel wide CTCPs (+C)
    private boolean noCtcp;

    // No external messages (+n)
    private boolean noExternal;

    // No channel notices (+N)
    private boolean noNotice;

    public boolean isHiddenUsers() {
        return hiddenUsers;
    }

    public boolean isNoControls() {
        return noControls;
    }

    public boolean isNoCtcp() {
        return noCtcp;
    }

    public boolean isNoExternal() {
        return noExternal;
    }

    public boolean isNoNotice() {
        return noNotice;
    }

    @Override
    public void process(boolean set, char mode, String... args) {
        if (mode == 'd') {
            hiddenUsers = set;
        } else if (mode == 'c') {
            noControls = set;
        } else if (mode == 'C') {
            noCtcp = set;
        } else if (mode == 'n') {
            noExternal = set;
        } else if (mode == 'N') {
            noNotice = set;
        } else {
            super.process(set, mode, args);
        }
    }

    public void setHiddenUsers(boolean hiddenUsers) {
        this.hiddenUsers = hiddenUsers;
    }

    public void setNoControls(boolean noControls) {
        this.noControls = noControls;
    }

    public void setNoCtcp(boolean noCtcp) {
        this.noCtcp = noCtcp;
    }

    public void setNoExternal(boolean noExternal) {
        this.noExternal = noExternal;
    }

    public void setNoNotice(boolean noNotice) {
        this.noNotice = noNotice;
    }

}
