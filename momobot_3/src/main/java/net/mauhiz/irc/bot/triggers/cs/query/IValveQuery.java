package net.mauhiz.irc.bot.triggers.cs.query;

import java.nio.ByteBuffer;

public interface IValveQuery {
    String QUERY = "Source Engine Query";

    void afterReceive(ByteBuffer resp);

    void beforeSend();

    ByteBuffer getCmd();
}
