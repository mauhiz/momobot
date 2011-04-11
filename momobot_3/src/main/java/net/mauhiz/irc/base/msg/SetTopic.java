package net.mauhiz.irc.base.msg;

import java.util.Date;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Topic;

public class SetTopic extends AbstractIrcMessage {

    private final String topic;

    public SetTopic(String from1, String to1, IrcServer server1, String newTopic) {
        super(from1, to1, server1);
        topic = newTopic;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("TOPIC ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        if (topic != null) {
            sb.append(" :");
            sb.append(topic);
        }
        return sb.toString();
    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    @Override
    public void process(IIrcControl control) {
        IrcChannel chan = server.findChannel(to);
        Topic newTopic = new Topic(from, new Date(), topic);
        chan.getProperties().setTopic(newTopic);
    }

    @Override
    public String toString() {
        return niceFromDisplay() + " changed the topic on " + to;
    }
}
