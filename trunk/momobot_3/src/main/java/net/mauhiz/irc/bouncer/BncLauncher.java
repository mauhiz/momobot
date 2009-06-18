package net.mauhiz.irc.bouncer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mauhiz.irc.base.ColorUtils;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetServer;
import net.mauhiz.util.AbstractRunnable;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class BncLauncher {
    private static final List<Account> ACCOUNTS = new ArrayList<Account>();
    static long globalStartTime;
    private static final String LOCAL_NICK = "root";
    private static final Logger LOG = Logger.getLogger(BncLauncher.class);
    private static final String METHOD = "NOTICE AUTH :";
    private static final int MY_PORT = 6667;
    private static final IrcServer QNET;
    
    static {
        QNET = new QnetServer("irc://uk.quakenet.org:6667/");
        QNET.setAlias("Quakenet");
        
        IrcUser myself = QNET.newUser("MomoBouncer");
        QNET.setMyself(myself);
    }
    
    /**
     * @param client
     * @return account
     * @throws IOException
     */
    static Account accept(BncClient client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.connection.getInputStream(),
                FileUtil.ISO8859_15));
        String nick = readNick(reader);
        if (nick == null) {
            return null;
        }
        
        PrintWriter writer = new PrintWriter(client.connection.getOutputStream());
        writer.println(":jbouncer 001 " + nick + " :Welcome to MomoBouncer");
        writer.println(":jbouncer 002 " + nick
                + " :This is an IRC proxy/bouncer. Unauthorized users must disconnect immediately.");
        writer.println(":jbouncer 003 " + nick + " :This bouncer has been up since " + new Date(globalStartTime));
        writer.println(":jbouncer 004 " + nick + " :");
        writer.println(":jbouncer 005 " + nick + " :");
        writer.println(METHOD + "Welcome to JBouncer. http://www.jibble.org/jbouncer/");
        writer.println(METHOD + "This is an IRC proxy/bouncer. Unauthorized users must disconnect immediately.");
        writer.println(METHOD
                + "To connect, enter your password by typing "
                + ColorUtils.toBold("/msg " + LOCAL_NICK + " " + ColorUtils.toUnderline("login") + " "
                        + ColorUtils.toUnderline("password")));
        // TODO auth stuff
        writer.flush();
        
        return readAccount(reader, writer, client);
        
    }
    
    private static void acceptConnections(ServerSocket bouncerServer) throws IOException {
        while (true) {
            BncClient client = new BncClient(bouncerServer.accept());
            Account acc = accept(client);
            if (acc == null) {
                LOG.warn("eeek");
            } else {
                acc.relatedManager.currentlyConnected.add(client);
                LOG.info(acc.username + " logged in successfully from "
                        + client.connection.getInetAddress().getHostName());
            }
        }
    }
    
    static void connectAccounts() {
        for (Account acc : ACCOUNTS) {
            IrcControl control = new IrcControl(acc.relatedManager);
            control.connect(acc.server);
        }
    }
    
    static void loadAccounts() {
        ACCOUNTS.clear();
        Account acc = new Account();
        acc.username = "mauhiz";
        acc.password = "mauhiz";
        /* beware */
        acc.server = QNET;
        /* cette technique ne marche que pour 1 seul account */
        IrcUser myself = acc.server.getMyself();
        myself.setUser(acc.username);
        myself.setNick(acc.username);
        ACCOUNTS.add(acc);
    }
    
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        globalStartTime = System.currentTimeMillis();
        loadAccounts();
        // connectAccounts();
        ServerSocket bouncerServer = new ServerSocket(MY_PORT);
        LOG.info("Bouncer server rock steady");
        acceptConnections(bouncerServer);
        
    }
    
    private static Account readAccount(BufferedReader reader, PrintWriter writer, BncClient client) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.toLowerCase().startsWith("privmsg " + LOCAL_NICK + " :")) {
                line = StringUtils.substringAfter(line, "privmsg " + LOCAL_NICK + " :");
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String login = parts[0];
                    String password = parts[1];
                    for (Account acc : ACCOUNTS) {
                        if (acc.username.equals(login) && acc.password.equals(password)) {
                            return acc;
                        }
                    }
                    LOG.info("Failed login attempt from " + client.connection.getInetAddress().getHostName() + " ("
                            + login + "/" + password + ")");
                }
                AbstractRunnable.sleep(1000);
                writer.println(METHOD + "Invalid login or password.");
            }
        }
        return null;
    }
    
    private static String readNick(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.startsWith("NICK ")) {
                return line.substring(5);
            }
        }
        return null;
    }
}
