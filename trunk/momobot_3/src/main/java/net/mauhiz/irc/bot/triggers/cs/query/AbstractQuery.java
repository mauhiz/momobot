package net.mauhiz.irc.bot.triggers.cs.query;

import java.nio.ByteBuffer;

import net.mauhiz.irc.bot.triggers.cs.IServer;

import org.apache.log4j.Logger;

public abstract class AbstractQuery implements IValveQuery {

    protected static final Logger LOG = Logger.getLogger(AbstractQuery.class);

    /**
     * @param buffer
     *            le bytebuffer
     * @return la chaine de caracteres
     */
    static String getNextString(ByteBuffer buffer) {
        StringBuilder retour = new StringBuilder();

        while (buffer.hasRemaining()) {
            /* et non pas buffer.getChar() */
            char temp = (char) buffer.get();
            if (0 == temp) {
                break;
            }
            retour.append(temp);
        }
        return retour.toString();
    }

    protected final IServer server;

    public AbstractQuery(IServer server) {
        this.server = server;
    }

    @Override
    public void beforeSend() {
        // empty by default;
    }
}
