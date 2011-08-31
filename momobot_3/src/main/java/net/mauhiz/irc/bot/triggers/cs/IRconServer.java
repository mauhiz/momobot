package net.mauhiz.irc.bot.triggers.cs;

import java.io.Closeable;
import java.io.IOException;

public interface IRconServer extends IServer, Closeable {

    boolean changelevel(String newMap) throws IOException;

    IRconClient getClient() throws IOException;

    void svRestart(int delay) throws IOException;

}
