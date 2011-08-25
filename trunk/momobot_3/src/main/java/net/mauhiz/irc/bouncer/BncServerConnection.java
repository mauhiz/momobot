package net.mauhiz.irc.bouncer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.ThreadUtils;

import org.apache.log4j.Logger;

public class BncServerConnection extends AbstractDaemon {
    private static final Logger LOG = Logger.getLogger(BncServerConnection.class);

    private final AccountStore accountStore;
    private long globalStartTime;
    final BncUser mySelf;
    private final int port;
    final BncServer serverData;

    public BncServerConnection(AccountStore accountStore, int port) {
        super("Bouncer Server");
        this.accountStore = accountStore;

        serverData = new BncServer("Bouncer");
        mySelf = serverData.newUser("root");
        this.port = port;
    }

    private void bouncerLoop(AsynchronousServerSocketChannel bouncerServer) throws TimeoutException {
        while (isRunning()) {
            AsynchronousSocketChannel socket;
            try {
                socket = bouncerServer.accept().get(4, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                ThreadUtils.handleInterruption(e);
                break;
            } catch (ExecutionException e) {
                LOG.error(e.getCause(), e.getCause());
                continue;
            }

            try {
                ClientPeer peer = new ClientPeer(socket, serverData);
                BncClientControl control = new BncClientControl();
                BncClientIO bcl = new BncClientIO(control, socket, peer, this);
                bcl.tstart();
            } catch (IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
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

    public long getGlobalStartTime() {
        return globalStartTime;
    }

    @Override
    public void trun() {
        try {
            AsynchronousServerSocketChannel bouncerServer = AsynchronousServerSocketChannel.open();
            bouncerServer.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
            LOG.info("Bouncer server rock steady");
            globalStartTime = System.currentTimeMillis();
            connectAccounts();
            bouncerLoop(bouncerServer);

        } catch (TimeoutException ste) {
            // nevermind
            LOG.debug(ste, ste);
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
        }

        tstop();
    }
}
