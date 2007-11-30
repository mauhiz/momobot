/*
 * Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/ This file is part of JBouncer. This software is
 * dual-licensed, allowing you to choose between the GNU General Public License (GPL) and the www.jibble.org Commercial
 * License. Since the GPL may be too restrictive for use in a proprietary application, a commercial license is also
 * provided. Full license information can be found at http://www.jibble.org/licenses/ $Author: pjm2 $ $Id:
 * ServerConnection.java,v 1.5 2004/03/02 00:32:27 pjm2 Exp $
 */
package bouncer;

import ircbot.AbstractIrcBot;
import ircbot.ColorsUtils;
import ircbot.IrcServerBean;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ServerConnection extends AbstractIrcBot {
    /**
     * 
     */
    private static final Logger       LOG            = Logger.getLogger(ServerConnection.class);
    /**
     * 
     */
    private List < ClientConnection > clients        = new LinkedList < ClientConnection >();
    /**
     * 
     */
    private List < String >           history        = new LinkedList < String >();
    /**
     * 
     */
    private IrcServerBean             server;
    /**
     * 
     */
    private String[]                  serverMessages = new String[6];
    /**
     * 
     */
    private User                      user;

    /**
     * @param server1
     * @param port1
     * @param password1
     * @param nick
     * @param user1
     */
    public ServerConnection(final String server1, final int port1, final String password1, final String nick,
            final User user1) {
        this.server = new IrcServerBean(server1, port1, password1);
        this.user = user1;
        setTargetName(nick);
        setLogin("JBouncer");
        LOG.info(user1.getLogin() + " initiated new connection to " + server1);
    }

    /**
     * @param client
     */
    public void add(final ClientConnection client) {
        synchronized (this.clients) {
            this.clients.add(client);
        }
        LOG.info(this.user.getLogin() + " attached to session on " + this.server);
        LOG.info(this.user.getLogin() + " is now using " + getClientCount() + " sessions.");
    }

    /**
     * @param line
     */
    public void addToHistory(final String line) {
        String[] parts = line.split("\\s+", 4);
        if (parts.length == 4) {
            String type = parts[1];
            if (type.equals("PRIVMSG")) {
                String from = parts[0];
                int index = from.indexOf('!');
                if (index > 1) {
                    from = from.substring(1, index);
                }
                String target = parts[2];
                String message = parts[3].substring(1);
                message = ColorsUtils.removeFormattingAndColors(message);
                String where = from;
                if ("#&!+".indexOf(target.charAt(0)) >= 0) {
                    where = target;
                }
                LOG.info(where, "<" + from + "> " + message);
                this.history.add(line);
            }
        }
        if (this.history.size() > JBouncerMain.HISTORY_LIMIT) {
            this.history.remove(0);
        }
    }

    /**
     * 
     */
    public void connect() {
        int tries = 0;
        boolean trying = true;
        while (trying) {
            tries++;
            try {
                sendToOtherClients(null, ClientConnection.METHOD + "JBouncer is trying to connect to " + this.server
                        + " (retry #" + tries + ")");
                connect(this.server);
                sendToOtherClients(null, ClientConnection.METHOD + "JBouncer is connected to " + this.server);
                return;
            } catch (Exception e) {
                sendToOtherClients(null, ClientConnection.METHOD
                        + "JBouncer could not connect. Trying again in 5 minutes.");
            }
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                // This should not happen.
            }
        }
    }

    /**
     * @return le nb de clients
     */
    public int getClientCount() {
        return this.clients.size();
    }

    /**
     * @return a new collection to avoid blocking or thread safety issues.
     */
    public List < String > getHistory() {
        return new LinkedList < String >(this.history);
    }

    /**
     * @return
     */
    public String[] getServerMessages() {
        return this.serverMessages;
    }

    /**
     * @see ircbot.AbstractIrcBot#onDisconnect()
     */
    @Override
    public void onDisconnect() {
        connect();
    }

    /**
     * @see ircbot.IIrcBot#onServerResponse(int, java.lang.String)
     */
    @Override
    public void onServerResponse(final int code, final String message) {
        if (code >= 0 && code <= 5) {
            this.serverMessages[code] = message;
        }
    }

    /**
     * @param client
     */
    public void removeClient(final ClientConnection client) {
        synchronized (this.clients) {
            this.clients.remove(client);
        }
        LOG.info(this.user.getLogin() + " detached from session on " + this.server);
    }

    /**
     * @param sendingClient
     * @param line
     */
    public void sendToOtherClients(final ClientConnection sendingClient, final String line) {
        synchronized (this.clients) {
            Iterator < ClientConnection > it = this.clients.iterator();
            while (it.hasNext()) {
                try {
                    ClientConnection client = it.next();
                    if (client != sendingClient) {
                        client.send(line);
                    }
                } catch (IOException e) {
                    it.remove();
                }
            }
        }
    }
}
