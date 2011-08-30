package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface IClient extends AutoCloseable {

    void close() throws IOException;

    void getInfo() throws IOException;

    void getPlayers() throws IOException;

    void getRules() throws IOException;

    void receive(ByteBuffer dest) throws IOException;
}
