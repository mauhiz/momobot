package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;

public interface IRconClient extends IClient {

    void changeRcon(String rcon);

    void getRconChallenge() throws IOException;

    void processLine(String line);

    void rconCmd(String cmd) throws IOException;

}
