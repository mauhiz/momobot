package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public interface IClient {

    void getInfo() throws IOException;

    void getPlayers() throws IOException;

    void getRules() throws IOException;

    SocketAddress receive(ByteBuffer dest) throws IOException;
}
