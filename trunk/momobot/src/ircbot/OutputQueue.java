package ircbot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import utils.MyRunnable;

/**
 * @author viper
 */
public class OutputQueue extends MyRunnable implements IIrcSpecialChars {
    /**
     * logger.
     */
    private static final Logger            LOG           = Logger
                                                                 .getLogger(OutputQueue.class);
    /**
     * Longueur maximale d'un message sur IRC.
     */
    private static final int               MAXLINELENGTH = 512;
    /**
     * Durée de la protection antiflood en secondes.
     */
    private static final int               MESSAGEDELAY  = 1000;
    /**
     * Taille de la file d'attente.
     */
    private static final int               QUEUE_SIZE    = 30;
    /**
     * Permet d'écrire au serveur.
     */
    private Writer                         bwriter       = null;
    /**
     * La file d'attente elle-même.
     */
    private final BlockingQueue < String > queue;

    /**
     * @param socket
     *            Le socket qu'on me donne
     */
    OutputQueue(final Socket socket) {
        this.queue = new LinkedBlockingQueue < String >(QUEUE_SIZE);
        try {
            this.bwriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())));
            setRunning(true);
        } catch (final Exception e) {
            LOG.fatal(e, e);
        }
    }

    /**
     * Place un element en queue.
     * @param string
     *            l'élément
     */
    final synchronized void put(final String string) {
        try {
            this.queue.offer(StringUtils.left(string, MAXLINELENGTH - 2) + CR
                    + LF, 1, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            LOG.fatal(e, e);
        }
    }

    /**
     * Crée une boucle d'attente.
     * @see java.lang.Runnable#run()
     */
    @Override
    public final void run() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("started");
        }
        String line;
        try {
            while (isRunning()) {
                /* antiflood */
                Thread.sleep(MESSAGEDELAY);
                /* méthode qui bloque */
                line = this.queue.poll(1, TimeUnit.SECONDS);
                if (line == null) {
                    /* arrivera quand le bot a rien sortir */
                    continue;
                    /*
                     * on repart pour une boucle; cela permet de voir si on est
                     * toujours running
                     */
                }
                try {
                    IOUtils.write(line, this.bwriter);
                    this.bwriter.flush();
                } catch (final IOException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(e, e);
                    }
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info(">> " + line);
                }
            }
        } catch (final InterruptedException e) {
            LOG.fatal(e, e);
            setRunning(false);
        } finally {
            IOUtils.closeQuietly(this.bwriter);
        }
    }
}
