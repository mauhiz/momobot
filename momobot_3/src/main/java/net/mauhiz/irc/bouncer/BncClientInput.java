package net.mauhiz.irc.bouncer;

import java.io.IOException;
import java.net.Socket;

import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.io.IrcInput;

/**
 * @author viper
 */
public class BncClientInput extends IrcInput {

    BncClientInput(IIrcIO io1, Socket socket) throws IOException {
        super(io1, socket);
        setName("Bnc Client Input");
    }
}
