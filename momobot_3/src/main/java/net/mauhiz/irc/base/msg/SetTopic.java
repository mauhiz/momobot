package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

public class SetTopic extends AbstractIrcMessage {

    private final String topic;

    public SetTopic(Target from, Target chan, IIrcServerPeer server, String newTopic) {
        super(from, chan, server);
        topic = newTopic;
    }

    @Override
    public SetTopic copy() {
        return new SetTopic(from, to, server, topic);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }

        sb.append(IrcCommands.TOPIC).append(' ');
        sb.append(super.to);

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
    public String toString() {
        return niceFromDisplay() + " changed the topic on " + to;
    }
}
