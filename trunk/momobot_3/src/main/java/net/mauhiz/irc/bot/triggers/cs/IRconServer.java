package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;

public interface IRconServer extends IServer, AutoCloseable {

    boolean changelevel(String newMap) throws IOException;

    IRconClient getClient() throws IOException;

    void svRestart(int delay) throws IOException;

}
