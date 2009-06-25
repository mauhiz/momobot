package net.mauhiz.irc.bot.triggers.cs.query;

public interface IValveQuery {
    String QUERY = "Source Engine Query";
    void afterReceive(byte[] resp);
    void beforeSend();
    byte[] getCmd();
}
