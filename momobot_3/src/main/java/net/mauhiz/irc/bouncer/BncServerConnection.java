package net.mauhiz.irc.bouncer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.util.AbstractRunnable;

import org.apache.log4j.Logger;

public class BncServerConnection extends AbstractRunnable {
    private static final Logger LOG = Logger.getLogger(BncServerConnection.class);
    /**
     * Timeout en millisecondes.
     */
    private static final int SO_TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(4, TimeUnit.MINUTES);
    private final AccountStore accountStore;
    private long globalStartTime;

    final BncUser mySelf;

    final BncServer serverData;

    public BncServerConnection(AccountStore accountStore, int port) {
        super();
        this.accountStore = accountStore;

        serverData = new BncServer(URI.create("irc://localhost:" + port + "/"));
        serverData.setAlias("Bouncer");

        mySelf = serverData.newUser("root");
        serverData.setMyself(mySelf);
    }

    private void connectAccounts() { // TODO implement limit
        for (Account acc : accountStore.getAccounts()) {
            IrcControl control = new IrcControl(acc.getRelatedManager());
            control.connect(acc.getServer());
        }
    }

    public AccountStore getAccountStore() {
        return accountStore;
    }

    @Override
    public void run() {
        try {
            ServerSocket bouncerServer = new ServerSocket(serverData.getAddress().getPort());
            LOG.info("Bouncer server rock steady");
            globalStartTime = System.currentTimeMillis();
            connectAccounts();
            while (isRunning()) {
                Socket socket = bouncerServer.accept();
                socket.setSoTimeout(SO_TIMEOUT);

                ClientPeer peer = new ClientPeer(socket, serverData);
                BncClientControl control = new BncClientControl();
                BncClientIO bcl = new BncClientIO(control, socket, peer, this);
                bcl.start();
            }
        } catch (SocketTimeoutException ste) {
            // nevermind
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
        }

        stop();
    }
}
