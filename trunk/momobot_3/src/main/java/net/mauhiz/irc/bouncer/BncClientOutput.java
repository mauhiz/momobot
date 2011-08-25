package net.mauhiz.irc.bouncer;

import java.nio.channels.AsynchronousSocketChannel;

import net.mauhiz.irc.base.io.IrcOutput;

/**
 * @author mauhiz
 */
public class BncClientOutput extends IrcOutput {

    public BncClientOutput(AsynchronousSocketChannel socket) {
        super(socket);
        setName("Bnc Client Output");
    }
}
