package net.mauhiz.irc.base.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ChannelProperties {
    private static final Logger LOG = Logger.getLogger(ChannelProperties.class);
    private final List<Mask> bans = new ArrayList<Mask>();
    private boolean inviteOnly;
    private String key;
    private Integer limit;
    private boolean moderated;
    private boolean noExt;
    private boolean opTopic;
    private boolean prive;
    private Topic topic;

    /**
     * @param ban
     *            the ban to set
     */
    public void addBan(Mask ban) {
        bans.add(ban);
    }

    /**
     * @return the bans
     */
    public List<Mask> getBans() {
        return bans;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @return the topic
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * @return the inviteOnly
     */
    public boolean isInviteOnly() {
        return inviteOnly;
    }

    /**
     * @return the moderated
     */
    public boolean isModerated() {
        return moderated;
    }

    /**
     * @return the noExt
     */
    public boolean isNoExt() {
        return noExt;
    }

    public boolean isOpTopic() {
        return opTopic;
    }

    /**
     * @return the prive
     */
    public boolean isPrive() {
        return prive;
    }

    public void process(boolean set, char mode, String... args) {
        if (mode == 't') {
            opTopic = set;
        } else {
            LOG.warn("TODO process mode=" + mode);
        }
    }

    /**
     * @param inviteOnly
     *            the inviteOnly to set
     */
    public void setInviteOnly(boolean inviteOnly) {
        this.inviteOnly = inviteOnly;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @param limit
     *            the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @param moderated
     *            the moderated to set
     */
    public void setModerated(boolean moderated) {
        this.moderated = moderated;
    }

    /**
     * @param noExt
     *            the noExt to set
     */
    public void setNoExt(boolean noExt) {
        this.noExt = noExt;
    }

    public void setOpTopic(boolean opTopic) {
        this.opTopic = opTopic;
    }

    /**
     * @param prive
     *            the prive to set
     */
    public void setPrive(boolean prive) {
        this.prive = prive;
    }

    /**
     * @param topic
     *            the topic to set
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
