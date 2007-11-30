/*
 * Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/ This file is part of JBouncer. This software is
 * dual-licensed, allowing you to choose between the GNU General Public License (GPL) and the www.jibble.org Commercial
 * License. Since the GPL may be too restrictive for use in a proprietary application, a commercial license is also
 * provided. Full license information can be found at http://www.jibble.org/licenses/ $Author: pjm2 $ $Id:
 * JBouncerMain.java,v 1.3 2004/03/01 19:13:37 pjm2 Exp $
 */
package bouncer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class JBouncerMain {
    /**
     * 
     */
    public static final int     HISTORY_LIMIT;
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(JBouncerMain.class);
    /**
     * 
     */
    private static final int    PORT;
    /**
     * 
     */
    public static final boolean TAKE_LOGS;
    static {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("./config.ini"));
        } catch (IOException e) {
            LOG.warn("Could not read the config file.");
            // throw new ExceptionInInitializerError();
        }
        String portStr = p.getProperty("Port");
        int port = 6667;
        if (portStr != null) {
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                // Keep the default value;
            }
        }
        PORT = port;
        String historyStr = p.getProperty("HistoryLimit");
        int i = 10;
        if (historyStr != null) {
            i = Integer.parseInt(historyStr);
        }
        HISTORY_LIMIT = i;
        LOG.info("The message history limit for each session is " + HISTORY_LIMIT);
        String logStr = p.getProperty("TakeLogs");
        if (logStr != null) {
            TAKE_LOGS = Boolean.parseBoolean(logStr);
        } else {
            TAKE_LOGS = false;
        }
        if (TAKE_LOGS) {
            LOG.info("Logging activated.");
        }
    }

    /**
     * @param args
     */
    public static void main(final String... args) {
        /* Populate the HashMap of bouncers (one per user). */
        Map < User, JBouncer > bouncers = new HashMap < User, JBouncer >();
        readAccounts(new File("./accounts.ini"), bouncers);
        JBouncerManager manager = new JBouncerManager(bouncers);
        // Now allow clients to connect.
        try {
            ClientListener listener = new ClientListener(manager, PORT);
            new Thread(listener).start();
        } catch (IOException e) {
            LOG.fatal("Cannot listen on port " + PORT);
            System.exit(1);
        }
        LOG.info("*** JBouncer ready to accept connections on port " + PORT);
    }

    /**
     * @param f
     * @param bouncers
     */
    static void readAccounts(final File f, final Map < User, JBouncer > bouncers) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = null;
            // int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String login = parts[0];
                    String password = parts[1];
                    User user = new User(login, password);
                    if (!login.startsWith("#") && !bouncers.containsKey(user)) {
                        bouncers.put(user, new JBouncer(user));
                        LOG.info("Created bouncer for " + login);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Unable to process " + f.getAbsolutePath(), e);
        }
    }
}
