package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;

public interface IRconServer extends IServer {

    boolean changelevel(String newMap) throws IOException;

    IRconClient getClient();

    void svRestart(int delay) throws IOException;

}
