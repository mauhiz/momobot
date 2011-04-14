package net.mauhiz.irc.bouncer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.base.IrcClientControl;
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

        serverData = new BncServer("Bouncer");
        mySelf = serverData.newUser("root");
    }

    private void connectAccounts() { // TODO implement limit
        for (Account acc : accountStore.getAccounts()) {
            IrcClientControl control = new IrcClientControl(acc.getRelatedManager());
            control.connect(acc.getServer().newServerPeer());
        }
    }

    public AccountStore getAccountStore() {
        return accountStore;
    }

    @Override
    public void run() {
        try {
            ServerSocket bouncerServer = new ServerSocket(6667);
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
