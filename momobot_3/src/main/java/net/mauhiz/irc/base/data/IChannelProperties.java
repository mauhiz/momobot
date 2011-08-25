package net.mauhiz.irc.base.data;

import java.util.List;

public interface IChannelProperties {

    /**
     * @param ban
     *            the ban to set
     */
    void addBan(HostMask ban);

    /**
     * @return the bans
     */
    List<HostMask> getBans();

    /**
     * @return the key
     */
    String getKey();

    /**
     * @return the limit
     */
    Integer getLimit();

    /**
     * @return the topic
     */
    Topic getTopic();

    /**
     * @return the inviteOnly
     */
    boolean isInviteOnly();

    /**
     * @return the moderated
     */
    boolean isModerated();

    /**
     * @return the noExt
     */
    boolean isNoExt();

    boolean isOpTopic();

    /**
     * @return the prive
     */
    boolean isPrive();

    void process(boolean set, int mode, String... args);

    /**
     * @param inviteOnly
     *            the inviteOnly to set
     */
    void setInviteOnly(boolean inviteOnly);

    /**
     * @param key
     *            the key to set
     */
    void setKey(String key);

    /**
     * @param limit
     *            the limit to set
     */
    void setLimit(Integer limit);

    /**
     * @param moderated
     *            the moderated to set
     */
    void setModerated(boolean moderated);

    /**
     * @param noExt
     *            the noExt to set
     */
    void setNoExt(boolean noExt);

    void setOpTopic(boolean opTopic);

    /**
     * @param prive
     *            the prive to set
     */
    void setPrive(boolean prive);

    /**
     * @param topic
     *            the topic to set
     */
    void setTopic(Topic topic);

}