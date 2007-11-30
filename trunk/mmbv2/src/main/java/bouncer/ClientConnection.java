/*
 * Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/ This file is part of JBouncer. This software is
 * dual-licensed, allowing you to choose between the GNU General Public License (GPL) and the www.jibble.org Commercial
 * License. Since the GPL may be too restrictive for use in a proprietary application, a commercial license is also
 * provided. Full license information can be found at http://www.jibble.org/licenses/ $Author: pjm2 $ $Id:
 * ClientConnection.java,v 1.3 2004/03/01 19:13:37 pjm2 Exp $
 */
package bouncer;

import ircbot.Channel;
import ircbot.IIrcCommands;
import ircbot.IIrcSpecialChars;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ClientConnection implements Runnable, IIrcSpecialChars {
    /**
     * 
     */
    private static final Logger LOG    = Logger.getLogger(ClientConnection.class);
    /**
     * 
     */
    public static final String  METHOD = "NOTICE AUTH :";
    /**
     * 
     */
    public static final String  NICK   = "root";
    /**
     * 
     */
    private JBouncerManager     manager;
    /**
     * 
     */
    private BufferedReader      reader;
    /**
     * 
     */
    private Socket              socket;
    /**
     * 
     */
    private BufferedWriter      writer;

    /**
     * @param socket1
     * @param manager1
     */
    public ClientConnection(final Socket socket1, final JBouncerManager manager1) {
        this.socket = socket1;
        this.manager = manager1;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        ServerConnection server = null;
        JBouncer bouncer = null;
        User user = null;
        boolean replayHistory = false;
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            String nick = null;
            String line = null;
            while ((line = this.reader.readLine()) != null) {
                if (line.startsWith("NICK ")) {
                    nick = line.substring(5);
                    break;
                }
            }
            if (nick == null) {
                return;
            }
            send(":jbouncer 001 " + nick + " :Welcome to MomoBouncer");
            send(":jbouncer 002 " + nick
                    + " :This is an IRC proxy/bouncer. Unauthorized users must disconnect immediately.");
            send(":jbouncer 003 " + nick + " :This bouncer has been up since " + new Date(this.manager.getStartTime()));
            // sendRawLine(":jbouncer 004 " + nick + " :");
            // sendRawLine(":jbouncer 005 " + nick + " :");
            // sendRawLine(METHOD + "Welcome to JBouncer. http://www.jibble.org/jbouncer/");
            // sendRawLine(METHOD + "This is an IRC proxy/bouncer. Unauthorized users must disconnect immediately.");
            send(METHOD + "To connect, enter your password by typing " + BOLD + "/msg " + NICK + " " + UNDERLINE
                    + "login" + UNDERLINE + " " + UNDERLINE + "password");
            while ((line = this.reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("privmsg " + NICK + " :")) {
                    line = line.substring(("privmsg " + NICK + " :").length());
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        String login = parts[0];
                        String password = parts[1];
                        user = new User(login, password);
                        bouncer = this.manager.getBouncer(user);
                        if (bouncer != null) {
                            break;
                        }
                        LOG.info("Failed login attempt from " + this.socket.getInetAddress().getHostName() + " ("
                                + user.getLogin() + "/" + user.getPassword() + ")");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Do nothing.
                    }
                    send(METHOD + "Invalid login or password.");
                }
            }
            if (line == null) {
                return;
            }
            LOG.info(user.getLogin() + " logged in successfully from " + this.socket.getInetAddress().getHostName());
            boolean gettingInput = true;
            while (gettingInput) {
                Map < String, ServerConnection > servers = bouncer.getServers();
                synchronized (servers) {
                    if (servers.size() > 0) {
                        send(METHOD + "Attach to one of the following servers by typing " + IIrcSpecialChars.BOLD
                                + "/msg " + NICK + " connect " + IIrcSpecialChars.UNDERLINE + "name");
                        Iterator < String > it = servers.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            server = servers.get(key);
                            String status = " (" + server.getClientCount() + " clients)";
                            if (!server.isConnected()) {
                                status = " [disconnected]";
                            }
                            send(METHOD + "    " + IIrcSpecialChars.BOLD + key + IIrcSpecialChars.BOLD + ": "
                                    + server.getNick() + " on " + server.getCurrentServer() + status);
                        }
                    }
                }
                send(METHOD + "Create a new IRC server connection by typing " + IIrcSpecialChars.BOLD + "/msg " + NICK
                        + " create " + IIrcSpecialChars.UNDERLINE + "name" + IIrcSpecialChars.UNDERLINE + " "
                        + IIrcSpecialChars.UNDERLINE + "server" + IIrcSpecialChars.UNDERLINE + " "
                        + IIrcSpecialChars.UNDERLINE + "[port]" + IIrcSpecialChars.UNDERLINE + " "
                        + IIrcSpecialChars.UNDERLINE + "[password]");
                line = this.reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.toLowerCase().startsWith("privmsg " + NICK + " :")) {
                    line = line.substring(("privmsg " + NICK + " :").length());
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2 && parts[0].toLowerCase().equals("connect")) {
                        String name = parts[1];
                        synchronized (servers) {
                            server = servers.get(name);
                        }
                        if (server != null) {
                            server.add(this);
                            replayHistory = true;
                            break;
                        }
                        send(METHOD + "There is no server with the name " + IIrcSpecialChars.BOLD + name);
                    } else if (parts.length == 2 && parts[0].toLowerCase().equals("remove")) {
                        String name = parts[1];
                        synchronized (bouncer) {
                            servers = bouncer.getServers();
                            server = servers.get(name);
                            if (server != null) {
                                if (server.getClientCount() == 0) {
                                    bouncer.remove(name);
                                } else {
                                    send(METHOD + "Cannot remove " + IIrcSpecialChars.BOLD + name
                                            + IIrcSpecialChars.BOLD
                                            + ", as there are clients connected to the session.");
                                }
                            } else {
                                send(METHOD + "Could not find server " + IIrcSpecialChars.BOLD + name
                                        + IIrcSpecialChars.BOLD + " to remove it.");
                            }
                        }
                    } else if (parts.length >= 3 && parts.length <= 5 && parts[0].toLowerCase().equals("create")) {
                        String name = parts[1];
                        String serverName = parts[2];
                        int port = 6667;
                        if (parts.length >= 4) {
                            try {
                                port = Integer.parseInt(parts[3]);
                            } catch (NumberFormatException e) {
                                // Do nothing; stick with the default value.
                            }
                        }
                        String password = null;
                        if (parts.length >= 5) {
                            password = parts[4];
                        }
                        synchronized (servers) {
                            if (servers.containsKey(name)) {
                                send(METHOD + "The name " + IIrcSpecialChars.BOLD + name + IIrcSpecialChars.BOLD
                                        + " is already used by another connection.");
                                continue;
                            }
                        }
                        server = new ServerConnection(serverName, port, password, nick, user);
                        server.add(this);
                        bouncer.add(name, server);
                        break;
                    } else {
                        send(METHOD + "Unrecognised command.");
                    }
                }
            }
            if (line == null) {
                return;
            }
            server.sendToOtherClients(this, ":" + NICK + "!jbouncer@jbouncer PRIVMSG " + nick + " :" + nick
                    + " connected to this session from " + this.socket.getInetAddress().getHostName());
            // Tell the client what its nick should be.
            send(":" + nick + "!jbouncer@jbouncer NICK :" + server.getNick());
            // Reply the server messages.
            for (int i = 1; i < 5; i++) {
                String serverMessage = server.getServerMessages()[i];
                if (serverMessage != null) {
                    send(serverMessage);
                }
            }
            if (!server.isConnected()) {
                send(METHOD + "JBouncer is not currently connected to " + server.getCurrentServer()
                        + ". JBouncer will keep trying to connect automatically every 5 minutes.");
            }
            for (Channel channel : Channel.getAll()) {
                send(":" + server.getNick() + "!jbouncer@jbouncer JOIN :" + channel);
                server.send("NAMES " + channel);
                server.send("TOPIC " + channel);
                server.send("MODE " + channel);
            }
        } catch (IOException e) {
            // Abandon the connection.
            return;
        }
        // Now connected to the server properly...
        String line = null;
        try {
            // Send off all of the messages from the past.
            if (replayHistory) {
                for (final String next : server.getHistory()) {
                    send(next);
                }
            }
            // Main loop.
            while ((line = this.reader.readLine()) != null) {
                // Don't let people disconnect!
                if (line.startsWith(IIrcCommands.QUIT)) {
                    continue;
                }
                if (line.startsWith(IIrcCommands.PRIVMSG + SPC) || line.startsWith("ACTION ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3 && parts[1].toLowerCase().equals(NICK.toLowerCase())) {
                        String nick = server.getNick();
                        String message = parts[2].substring(1).trim();
                        if (message.toLowerCase().equals("info")) {
                            sendPrivateMessage(nick, IIrcSpecialChars.BOLD + "This Session" + IIrcSpecialChars.BOLD);
                            sendPrivateMessage(nick, " Clients connected to this session: " + server.getClientCount());
                            sendPrivateMessage(nick, " This session is connected to " + server.getCurrentServer());
                            sendPrivateMessage(nick, IIrcSpecialChars.BOLD + "This JBouncer" + IIrcSpecialChars.BOLD);
                            sendPrivateMessage(nick, " Registered users: " + this.manager.getBouncers().size());
                            sendPrivateMessage(nick, " Up since: " + new Date(this.manager.getStartTime()));
                            sendPrivateMessage(nick, " Server connections: " + bouncer.getServers().size());
                        }
                        // stop the message going to anyone on the real server.
                        continue;
                    }
                    String copiedLine = ":" + server.getNick() + "!jbouncer@jbouncer " + line;
                    server.addToHistory(copiedLine);
                    server.sendToOtherClients(this, copiedLine);
                }
                server.send(line);
            }
        } catch (IOException e) {
            // Just let the client disappear.
        }
        try {
            // Tidy up.
            this.socket.close();
        } catch (IOException e) {
            // Do nothing.
        }
        server.removeClient(this);
    }

    /**
     * @param line
     * @throws IOException
     */
    public void send(final String line) throws IOException {
        this.writer.write(line + "\r\n");
        this.writer.flush();
    }

    /**
     * @param nick
     * @param message
     * @throws IOException
     */
    private void sendPrivateMessage(final String nick, final String message) throws IOException {
        send(":" + NICK + "!jbouncer@jbouncer PRIVMSG " + nick + " :" + message);
    }
}
