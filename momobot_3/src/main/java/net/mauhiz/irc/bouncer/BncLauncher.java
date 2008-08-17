package net.mauhiz.irc.bouncer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mauhiz.irc.base.ColorUtils;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.IrcServer;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class BncLauncher {
    static final List<Account> ACCOUNTS = new ArrayList<Account>();
    static long globalStartTime;
    static final String LOCAL_NICK = "root";
    private static final Logger LOG = Logger.getLogger(BncLauncher.class);
    static final String METHOD = "NOTICE AUTH :";
    static final int MY_PORT = 6667;
    static final IrcServer QNET;
    
    static {
        QNET = new IrcServer("irc://uk.quakenet.org:6667/");
        QNET.setMyFullName("MomoBouncer");
        QNET.setAlias("Quakenet");
    }
    
    /**
     * @param client
     * @return account
     * @throws IOException
     */
    static Account accept(final BncClient client) throws IOException {
        String nick = null;
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.connection.getInputStream()));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.connection.getOutputStream()));
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("NICK ")) {
                nick = line.substring(5);
                break;
            }
        }
        if (nick == null) {
            return null;
        }
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
        // /* TODO auth stuff */
        writer.flush();
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().startsWith("privmsg " + LOCAL_NICK + " :")) {
                line = line.substring(("privmsg " + LOCAL_NICK + " :").length());
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Do nothing.
                }
                writer.println(METHOD + "Invalid login or password.");
            }
        }
        return null;
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
        acc.server.setMyLogin(acc.username);
        acc.server.setMyNick(acc.username);
        ACCOUNTS.add(acc);
    }
    
    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        globalStartTime = System.currentTimeMillis();
        loadAccounts();
        // connectAccounts();
        ServerSocket bouncerServer = new ServerSocket(MY_PORT);
        LOG.info("Bouncer server rock steady");
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
}
