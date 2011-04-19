package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.SocketAddress;

public interface IServer {

    int getChallenge();

    void getDetails() throws IOException;

    String getIp();

    SocketAddress getIpay();

    String getMap();

    String getName();

    Iterable<Player> getPlayers();

    int getPort();

    void resetPlayers();

    void setChallenge(int challenge);

    void setMap(String map);

    void setMaxPlayers(int maxPlayers);

    void setName(String name);

    void setPing(long time);

    void setPlayer(int index, Player player);

    void setPlayerCount(int nbPlayers);

}
