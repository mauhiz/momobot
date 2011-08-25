package net.mauhiz.irc.bouncer;

import java.nio.channels.AsynchronousSocketChannel;

import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.io.IrcInput;

/**
 * @author viper
 */
public class BncClientInput extends IrcInput {

    BncClientInput(IIrcIO io1, AsynchronousSocketChannel socket) {
        super(io1, socket);
        setName("Bnc Client Input");
    }
}
