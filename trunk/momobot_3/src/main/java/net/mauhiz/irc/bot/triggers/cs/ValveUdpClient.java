package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import net.mauhiz.irc.bot.triggers.cs.query.ChallengeQuery;
import net.mauhiz.irc.bot.triggers.cs.query.IValveQuery;
import net.mauhiz.irc.bot.triggers.cs.query.InfoQuery;
import net.mauhiz.irc.bot.triggers.cs.query.PingQuery;
import net.mauhiz.irc.bot.triggers.cs.query.PlayersQuery;
import net.mauhiz.irc.bot.triggers.cs.query.RulesQuery;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
class ValveUdpClient implements IClient {

    /**
     * logger.
     */
    protected static final Logger LOG = Logger.getLogger(ValveUdpClient.class);

    private final DatagramChannel channel;

    /**
     * Serveur associe.
     */
    protected final IServer server;

    /**
     * @param server1
     *            le serveur
     */
    public ValveUdpClient(IServer server1) throws IOException {
        super();
        server = server1;
        channel = DatagramChannel.open();
    }

    private void doQuery(IValveQuery qry) throws IOException {
        qry.beforeSend();
        ByteBuffer result = sendAndRcvValveCmd(qry.getCmd());
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

    @Override
    public SocketAddress receive(ByteBuffer dest) throws IOException {
        SocketAddress ret = channel.receive(dest);
        dest.getInt(); // skip 1 int
        return ret;
    }

    /**
     * @param cmd
     *            la commande
     * @return un resultat
     * @throws IOException
     *             en cas de prob rezo
     */
    protected ByteBuffer sendAndRcvValveCmd(ByteBuffer cmd) throws IOException {
        sendValveCmd(cmd);
        ByteBuffer ret = ByteBuffer.allocate(FileUtil.BUF_SIZE);
        receive(ret);
        return ret;
    }

    protected ByteBuffer sendAndRcvValveCmd(String str) throws IOException {
        LOG.info("Sending and receiving : " + str + " to " + server.getIp() + ":" + server.getPort());
        return sendAndRcvValveCmd(FileUtil.ASCII.encode(str));
    }

    private void sendValveCmd(ByteBuffer cmdBytes) throws IOException {
        /* timeout : 10s */

        ByteBuffer sendBuf = ByteBuffer.allocate(cmdBytes.limit() + 5);
        sendBuf.putInt(NumberUtils.INTEGER_MINUS_ONE.intValue());
        sendBuf.put(cmdBytes);
        sendBuf.put(NumberUtils.INTEGER_ZERO.byteValue());
        channel.send(sendBuf, server.getIpay());
    }

    protected void sendValveCmd(String str) throws IOException {
        LOG.info("Sending and receiving : " + str + " to " + server.getIp() + ":" + server.getPort());
        sendValveCmd(FileUtil.ASCII.encode(str));
    }
}
