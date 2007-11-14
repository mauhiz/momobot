package ircbot;

import java.util.Calendar;

/**
 * Simple objet de stockage des informations relatives à un topic.
 * 
 * @author mauhiz
 */
public class Topic {
    /**
     * Date à laquelle le topic a été set.
     */
    private final Calendar date = Calendar.getInstance();
    /**
     * Le mec qui a set le topic. Comme il est sans doute plus sur IRC, on a juste son nick, pas d'objet {@link IrcUser}.
     * C'est d'ailleurs une faille du système d'authentification des users sur IRC.
     */
    private String         setBy;
    /**
     * Indique si le topic est protégé contre les non-ops.
     */
    private boolean        topicProtected;
    /**
     * Contenu du topic.
     */
    private String         value;

    /**
     * @param topic1
     *            le tropic
     * @param date1
     *            la date
     * @param setBy1
     *            celui qui a mis le topic
     */
    public Topic(final String topic1, final long date1, final String setBy1) {
        this.value = topic1;
        this.date.setTimeInMillis(date1);
        this.setBy = setBy1;
    }

    /**
     * @return la date
     */
    public final long getDate() {
        return this.date.getTimeInMillis();
    }

    /**
     * @return Returns the setBy.
     */
    public final String getSetBy() {
        return this.setBy;
    }

    /**
     * @return le topic
     */
    public final String getValue() {
        return this.value;
    }

    /**
     * @return Returns the topicProtected.
     */
    public final boolean isTopicProtected() {
        return this.topicProtected;
    }

    /**
     * @param date1
     *            la date
     */
    public final void setDate(final long date1) {
        this.date.setTimeInMillis(date1);
    }

    /**
     * @param setBy1
     *            The setBy to set.
     */
    public final void setSetBy(final String setBy1) {
        this.setBy = setBy1;
    }

    /**
     * @param topicProtected1
     *            The topicProtected to set.
     */
    public final void setTopicProtection(final boolean topicProtected1) {
        this.topicProtected = topicProtected1;
    }

    /**
     * @param value1
     *            le topic
     */
    public final void setValue(final String value1) {
        this.value = value1;
    }
}
