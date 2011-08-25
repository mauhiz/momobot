package net.mauhiz.irc.base.ident;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.CharacterCodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IdentServer extends AbstractDaemon implements IIdentServer {

    private static final Logger LOGGER = Logger.getLogger(IdentServer.class);
    /**
     * listening port
     */
    private static final int PORT = 113;
    private AsynchronousServerSocketChannel ss;
    private final String user;

    /**
     * @param user1
     */
    public IdentServer(IrcUser user1) {
        super("Ident Server");
        user = user1.getMask().getUser();
    }

    private void ident(Future<AsynchronousSocketChannel> socketFuture) throws CharacterCodingException,
            InterruptedException, ExecutionException, TimeoutException {
        AsynchronousSocketChannel socket = socketFuture.get(5, TimeUnit.SECONDS);
        ByteBuffer bb = ByteBuffer.allocate(4096);
        Integer bytesRead = socket.read(bb).get(5, TimeUnit.SECONDS);
        if (bytesRead.intValue() != -1) {
            CharBuffer cb = FileUtil.ASCII.newDecoder().decode(bb);
            cb.put(" : USERID : UNIX : " + user);
            socket.write(FileUtil.ASCII.newEncoder().encode(cb));
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {

        try {
            ss = AsynchronousServerSocketChannel.open();
            ss.bind(new InetSocketAddress("localhost", PORT), 1);

        } catch (IOException ioe) {
            LOGGER.error(ioe, ioe);
        }

        if (ss != null) {
            try {
                ident(ss.accept());
            } catch (InterruptedException e) {
                handleInterruption(e);
            } catch (ExecutionException e) {
                LOGGER.error(e.getCause(), e.getCause());
            } catch (TimeoutException e) {
                LOGGER.info("Server did not connect to me in time", e);
            } catch (CharacterCodingException e) {
                LOGGER.error(e, e);
            }
        }
        tstop();
    }

    @Override
    public void tstop() {
        super.tstop();
        if (ss != null) {
            try {
                ss.close();

            } catch (IOException ioe) {
                LOGGER.warn(ioe, ioe);
            }
        }
    }
}
