package net.mauhiz.irc.base.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ChannelProperties implements IChannelProperties {
    private static final Logger LOG = Logger.getLogger(ChannelProperties.class);
    private final List<HostMask> bans = new ArrayList<HostMask>();
    private boolean inviteOnly;
    private String key;
    private Integer limit;
    private boolean moderated;
    private boolean noExt;
    private boolean opTopic;
    private boolean prive;
    private Topic topic;

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#addBan(net.mauhiz.irc.base.data.HostMask)
     */
    public void addBan(HostMask ban) {
        bans.add(ban);
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#getBans()
     */
    public List<HostMask> getBans() {
        return bans;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#getKey()
     */
    public String getKey() {
        return key;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#getLimit()
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#getTopic()
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#isInviteOnly()
     */
    public boolean isInviteOnly() {
        return inviteOnly;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#isModerated()
     */
    public boolean isModerated() {
        return moderated;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#isNoExt()
     */
    public boolean isNoExt() {
        return noExt;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#isOpTopic()
     */
    public boolean isOpTopic() {
        return opTopic;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#isPrive()
     */
    public boolean isPrive() {
        return prive;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#process(boolean, char, java.lang.String[])
     */
    public void process(boolean set, char mode, String... args) {
        if (mode == 'b') {
            HostMask hm = HostMask.getInstance(args[0]);
            if (set) {
                bans.add(hm);
            } else {
                bans.remove(hm);
            }
        } else if (mode == 'i') {
            inviteOnly = set;
        } else if (mode == 'k') {
            key = set ? args[0] : null;
        } else if (mode == 'l') {
            limit = set ? Integer.valueOf(args[0]) : null;
        } else if (mode == 'm') {
            moderated = set;
        } else if (mode == 'n') {
            noExt = set;
        } else if (mode == 't') {
            opTopic = set;
        } else {
            LOG.warn("TODO process mode=" + mode);
        }
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setInviteOnly(boolean)
     */
    public void setInviteOnly(boolean inviteOnly) {
        this.inviteOnly = inviteOnly;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setKey(java.lang.String)
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setLimit(java.lang.Integer)
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setModerated(boolean)
     */
    public void setModerated(boolean moderated) {
        this.moderated = moderated;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setNoExt(boolean)
     */
    public void setNoExt(boolean noExt) {
        this.noExt = noExt;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setOpTopic(boolean)
     */
    public void setOpTopic(boolean opTopic) {
        this.opTopic = opTopic;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setPrive(boolean)
     */
    public void setPrive(boolean prive) {
        this.prive = prive;
    }

    /**
     * @see net.mauhiz.irc.base.data.IChannelProperties#setTopic(net.mauhiz.irc.base.data.Topic)
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
