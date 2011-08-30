package net.mauhiz.irc.base.ident;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.util.AbstractDaemon;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IdentServer extends AbstractDaemon implements IIdentServer {
    private static final Logger LOG = Logger.getLogger(IdentServer.class);

    /**
     * listening port
     */
    private static final int PORT = 113;
    private final String user;

    /**
     * @param user1
     */
    public IdentServer(IrcUser user1) {
        super("Ident Server");
        user = user1.getMask().getUser();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {
        try (AsynchronousServerSocketChannel ss = AsynchronousServerSocketChannel.open()) {
            ss.bind(new InetSocketAddress((InetAddress) null, PORT), 1);
            ss.accept(ss, new SocketAccepted(user));
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
        }
        tstop();
    }
}
