package ircbot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import utils.MyRunnable;
import utils.Utils;

/**
 * @author viper
 */
public class OutputQueue extends MyRunnable implements IIrcSpecialChars {
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
    private static final int               QUEUESIZE     = 30;
    /**
     * Permet d'écrire au serveur.
     */
    private BufferedWriter                 bwriter       = null;
    /**
     * La file d'attente elle-même.
     */
    private final BlockingQueue < String > queue         = new LinkedBlockingQueue < String >(
                                                                 QUEUESIZE);

    /**
     * @param socket
     *            Le socket qu'on me donne
     */
    OutputQueue(final Socket socket) {
        try {
            this.bwriter = new BufferedWriter(new OutputStreamWriter(socket
                    .getOutputStream()));
            setRunning(true);
        } catch (final Exception e2) {
            Utils.logError(this.getClass(), e2);
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
            Utils.logError(getClass(), e);
        }
    }

    /**
     * Crée une boucle d'attente.
     * @see java.lang.Runnable#run()
     */
    @Override
    public final void run() {
        Utils.log(getClass(), "started");
        String line;
        try {
            while (isRunning()) {
                // antiflood
                Thread.sleep(MESSAGEDELAY);
                // methode qui bloque
                line = this.queue.poll(1, TimeUnit.SECONDS);
                if (line == null) {
                    // arrivera quand le bot a rien sortir
                    continue;
                    // on repart pour une boucle; cela permet de voir si on est
                    // toujours running
                }
                try {
                    IOUtils.write(line, this.bwriter);
                    this.bwriter.flush();
                } catch (final IOException e) {
                    Utils.logError(getClass(), e);
                }
                Utils.log(getClass(), ">> " + line);
            }
        } catch (final InterruptedException e) {
            Utils.logError(getClass(), e);
            setRunning(false);
        }
    }
}
