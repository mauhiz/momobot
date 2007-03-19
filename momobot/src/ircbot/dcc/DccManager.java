package ircbot.dcc;

import ircbot.ACtcp;
import ircbot.IrcUser;
import ircbot.AIrcBot;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import utils.MyRunnable;

/**
 * This class is used to process DCC events from the server.
 * @since 1.2.0
 * @author Paul James Mutton, <a
 *         href="http://www.jibble.org/">http://www.jibble.org/</a>
 * @version 1.4.4 (Build time: Tue Mar 29 20:58:46 2005)
 */
public class DccManager implements IDccSubCommands {
    /**
     * mes DCC à reprendre.
     */
    private final List < DccFileTransfer > awaitingResume = new Vector < DccFileTransfer >();
    /**
     * mon bot.
     */
    private final AIrcBot                  bot;

    /**
     * @param lebot
     *            le bot
     */
    public DccManager(final AIrcBot lebot) {
        this.bot = lebot;
    }

    /**
     * Add this DccFileTransfer to the list of those awaiting possible resuming.
     * @param transfer
     *            the DccFileTransfer that may be resumed.
     */
    final void addAwaitingResume(final DccFileTransfer transfer) {
        synchronized (this.awaitingResume) {
            this.awaitingResume.add(transfer);
        }
    }

    /**
     * Processes a DCC request.
     * @param user
     *            un user
     * @param request
     *            une requete
     * @return True if the type of request was handled successfully.
     */
    public final boolean process(final IrcUser user, final String request) {
        final StringTokenizer tokenizer = new StringTokenizer(request);
        final String temp1 = tokenizer.nextToken();
        final String type = temp1 + tokenizer.nextToken();
        final String filename = tokenizer.nextToken();
        if (type.equals(DCC_SEND)) {
            final long unsignedLongAddress = Long.parseLong(tokenizer
                    .nextToken());
            final int unsignedShortPort = Integer.parseInt(tokenizer
                    .nextToken());
            final long unsignedLongSize = Long.parseLong(tokenizer.nextToken());
            final DccFileTransfer transfer = new DccFileTransfer(this.bot,
                    user, type, filename, unsignedLongAddress,
                    unsignedShortPort, unsignedLongSize);
            this.bot.onIncomingFileTransfer(transfer);
        } else if (type.equals(DCC_RESUME)) {
            final int port = Integer.parseInt(tokenizer.nextToken());
            final long progress = Long.parseLong(tokenizer.nextToken());
            DccFileTransfer transfer = null;
            synchronized (this.awaitingResume) {
                for (int i = 0; i < this.awaitingResume.size(); i++) {
                    transfer = this.awaitingResume.get(i);
                    if (transfer.getUser().equals(user)
                            && transfer.getPort() == port) {
                        this.awaitingResume.remove(i);
                        break;
                    }
                }
            }
            if (transfer != null) {
                transfer.resetProgress();
                transfer.updateProgress(progress);
                ACtcp.sendCtcpMsg(DCC_ACCEPT + SPC + "file.ext" + SPC + port
                        + SPC + progress, user.getNick(), this.bot
                        .getOutputThread());
            }
        } else if (type.equals(DCC_ACCEPT)) {
            final int port = Integer.parseInt(tokenizer.nextToken());
            /* long progress = */Long.parseLong(tokenizer.nextToken());
            DccFileTransfer transfer = null;
            synchronized (this.awaitingResume) {
                for (int i = 0; i < this.awaitingResume.size(); i++) {
                    transfer = this.awaitingResume.get(i);
                    if (transfer.getUser().equals(user)
                            && transfer.getPort() == port) {
                        this.awaitingResume.remove(i);
                        break;
                    }
                }
            }
            if (transfer != null) {
                transfer.doReceive(transfer.getFile(), true);
            }
        } else if (type.equals(DCC_CHAT)) {
            final long address = Long.parseLong(tokenizer.nextToken());
            final int port = Integer.parseInt(tokenizer.nextToken());
            final DccChat chat = new DccChat(user, address, port);
            new MiniThread(this.bot, chat).execute();
        } else {
            return false;
        }
        return true;
    }

    /**
     * Remove this transfer from the list of those awaiting resuming.
     * @param transfer
     *            le transfert à retirer
     */
    final void removeAwaitingResume(final DccFileTransfer transfer) {
        this.awaitingResume.remove(transfer);
    }
}

/**
 * @author viper
 */
class MiniThread extends MyRunnable {
    /**
     * mon bot.
     */
    private final AIrcBot bot;
    /**
     * mon minet.
     */
    private final DccChat chat;

    /**
     * @param chat1
     *            un matou.
     * @param lebot
     *            le bot
     */
    public MiniThread(final AIrcBot lebot, final DccChat chat1) {
        this.chat = chat1;
        this.bot = lebot;
    }

    /**
     * @see utils.MyRunnable#run()
     */
    @Override
    public void run() {
        this.bot.onIncomingChatRequest(this.chat);
    }
}
