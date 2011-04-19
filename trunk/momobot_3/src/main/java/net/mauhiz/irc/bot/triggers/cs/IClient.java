package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.DatagramPacket;

public interface IClient {

    void getInfo() throws IOException;

    void getPlayers() throws IOException;

    void getRules() throws IOException;

    void receive(DatagramPacket receivePacket) throws IOException;
}
