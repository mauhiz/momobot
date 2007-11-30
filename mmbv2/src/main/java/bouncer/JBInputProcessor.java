/**
 * 
 */
package bouncer;

import ircbot.InputProcessor;

import java.net.Socket;

/**
 * @author mauhiz
 */
public class JBInputProcessor extends InputProcessor {
    /**
     * @param bot1
     * @param socket
     */
    public JBInputProcessor(final ServerConnection bot1, final Socket socket) {
        super(bot1, socket);
    }

    /**
     * @see ircbot.InputProcessor#handleLine(java.lang.String)
     */
    @Override
    protected void handleLine(final String line) {
        ((ServerConnection) getBot()).addToHistory(line);
        ((ServerConnection) getBot()).sendToOtherClients(null, line);
        super.handleLine(line);
    }
}
