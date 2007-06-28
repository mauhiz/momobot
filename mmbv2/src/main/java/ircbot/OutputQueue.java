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

import utils.AbstractRunnable;

/**
 * @author mauhiz
 */
public class OutputQueue extends AbstractRunnable implements IIrcSpecialChars {
    /**
     * logger.
     */
    private static final Logger            LOG           = Logger.getLogger(OutputQueue.class);
    /**
     * Longueur maximale d'un message sur IRC.
     */
    private static final char              MAXLINELENGTH = 512;
    /**
     * Durée de la protection antiflood en millisecondes.
     */
    private static final long              MESSAGEDELAY  = 1000;
    /**
     * Taille de la file d'attente.
     */
    private static final char              QUEUE_SIZE    = 30;
    /**
     * Permet d'écrire au serveur.
     */
    private Writer                         bwriter;
    /**
     * La file d'attente elle-même.
     */
    private final BlockingQueue < String > queue         = new LinkedBlockingQueue < String >(QUEUE_SIZE);

    /**
     * @param socket
     *            Le socket qu'on me donne
     */
    protected OutputQueue(final Socket socket) {
        super();
        try {
            this.bwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            setRunning(true);
        } catch (final IOException ioe) {
            LOG.fatal(ioe, ioe);
            IOUtils.closeQuietly(this.bwriter);
        }
    }

    /**
     * Place un element en queue.
     * @param string
     *            l'élément
     */
    protected final void put(final String string) {
        try {
            synchronized (this.queue) {
                /* TODO on fait quoi de cette valeur de retour */
                this.queue.offer(StringUtils.left(string, MAXLINELENGTH - 2) + CR + LF, 1, TimeUnit.SECONDS);
            }
        } catch (final InterruptedException ie) {
            LOG.fatal(ie, ie);
        }
    }

    /**
     * Crée une boucle d'attente.
     * @see Runnable#run()
     */
    @Override
    public final void run() {
        LOG.debug("started");
        String line;
        try {
            while (isRunning()) {
                /* antiflood */
                pause(MESSAGEDELAY);
                /* méthode qui bloque */
                line = this.queue.poll(1, TimeUnit.SECONDS);
                if (null == line) {
                    /* arrivera quand le bot a rien sortir */
                    continue;
                    /* on repart pour une boucle; cela permet de voir si on est toujours running */
                }
                try {
                    IOUtils.write(line, this.bwriter);
                    this.bwriter.flush();
                } catch (final IOException ioe) {
                    LOG.error(ioe, ioe);
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info(">> " + line);
                }
            }
        } catch (final InterruptedException ie) {
            LOG.fatal(ie, ie);
            setRunning(false);
        } finally {
            IOUtils.closeQuietly(this.bwriter);
        }
    }
}
