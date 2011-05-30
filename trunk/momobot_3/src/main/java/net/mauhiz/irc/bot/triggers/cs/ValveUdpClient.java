package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import net.mauhiz.irc.bot.triggers.cs.query.ChallengeQuery;
import net.mauhiz.irc.bot.triggers.cs.query.IValveQuery;
import net.mauhiz.irc.bot.triggers.cs.query.InfoQuery;
import net.mauhiz.irc.bot.triggers.cs.query.PingQuery;
import net.mauhiz.irc.bot.triggers.cs.query.PlayersQuery;
import net.mauhiz.irc.bot.triggers.cs.query.RulesQuery;
import net.mauhiz.util.FileUtil;
import net.mauhiz.util.NetUtils;

import org.apache.commons.net.DatagramSocketClient;
import org.apache.commons.net.DefaultDatagramSocketFactory;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
class ValveUdpClient extends DatagramSocketClient implements IClient {

    /**
     * logger.
     */
    protected static final Logger LOG = Logger.getLogger(ValveUdpClient.class);

    /**
     * Serveur associe.
     */
    protected final IServer server;

    /**
     * @param server1
     *            le serveur
     */
    public ValveUdpClient(IServer server1) {
        super();
        server = server1;
    }

    private void doQuery(IValveQuery qry) throws IOException {
        qry.beforeSend();
        byte[] result = sendAndRcvValveCmd(qry.getCmd());
        qry.afterReceive(result);
    }

    /**
     * effectue un Challenge
     * @throws IOException
     *             houla, ca va mal
     */
    public void getChallenge() throws IOException {
        doQuery(new ChallengeQuery(server));
    }

    /**
     * fetch les infos
     * @throws IOException
     *             houla, ca va mal
     */
    public void getInfo() throws IOException {
        doQuery(new InfoQuery(server));
    }

    /**
     * fetch le ping
     * @throws IOException
     *             houla, ca va mal
     */
    public void getPing() throws IOException {
        doQuery(new PingQuery(server));
    }

    /**
     * fetch les players
     * @throws IOException
     *             houla, ca va mal
     */
    public void getPlayers() throws IOException {
        doQuery(new PlayersQuery(server));
    }

    /**
     * fetch les cvars
     * @throws IOException
     *             houla, ca va mal
     */
    public void getRules() throws IOException {
        doQuery(new RulesQuery(server));
    }

    /**
     * @return la socket
     */
    protected DatagramSocket getSocket() {
        return _socket_;
    }

    public void receive(DatagramPacket receivePacket) throws IOException {
        getSocket().receive(receivePacket);
    }

    /**
     * @param cmd
     *            la commande
     * @return un resultat
     * @throws IOException
     *             en cas de prob rezo
     */
    protected byte[] sendAndRcvValveCmd(byte[] cmd) throws IOException {
        sendValveCmd(cmd);
        DatagramPacket recPacket = NetUtils.createDatagramPacket();
        _socket_.receive(recPacket);
        return recPacket.getData();
    }

    protected byte[] sendAndRcvValveCmd(String str) throws IOException {
        return sendAndRcvValveCmd(FileUtil.getBytes(str, FileUtil.ASCII));
    }

    private void sendValveCmd(byte[] cmdBytes) throws IOException {
        if (null == _socket_) {
            _socket_ = new DefaultDatagramSocketFactory().createDatagramSocket();
            /* timeout en ms : 10s */
            _socket_.setSoTimeout(10000);
        }

        ByteBuffer sendBuf = ByteBuffer.allocate(cmdBytes.length + 5);
        sendBuf.putInt(-1);
        sendBuf.put(cmdBytes);
        sendBuf.put((byte) 0);
        LOG.info("Sending : " + new String(cmdBytes) + " to " + server.getIp() + ":" + server.getPort());
        DatagramPacket sendPacket = new DatagramPacket(sendBuf.array(), sendBuf.capacity(), server.getIpay());
        _socket_.send(sendPacket);
    }

    protected void sendValveCmd(String string) throws IOException {
        sendValveCmd(FileUtil.getBytes(string, FileUtil.ASCII));
    }
}
