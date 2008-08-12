package net.mauhiz.irc.base.data;

import net.mauhiz.irc.base.data.qnet.QnetChannelProperties;

/**
 * @author mauhiz
 */
public class Channel {
    String nom;
    Character prefix;
    ChannelProperties props = new QnetChannelProperties();

    public Channel(final String chanName) {
        if (Character.isLetterOrDigit(chanName.charAt(0))) {
            this.nom = chanName;
        }
        else {
            this.prefix = Character.valueOf(chanName.charAt(0));
            this.nom = chanName.substring(1);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.prefix == null) {
            return this.nom;
        }
        return this.prefix + this.nom;
    }
}
