package ircbot.dcc;

import ircbot.AbstractIrcBot;
import ircbot.CtcpUtils;
import ircbot.IIrcBot;
import ircbot.IrcUser;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.text.StrTokenizer;

import utils.AbstractRunnable;

/**
 * This class is used to process DCC events from the server.
 * @author Paul James Mutton, <a href="http://www.jibble.org/">http://www.jibble.org/</a>
 */
public class DccManager implements IDccSubCommands {
    /**
     * 
     */
    private static final long              serialVersionUID = 1;
    /**
     * mon bot.
     */
    private final IIrcBot                  bot;
    /**
     * 
     */
    private final List < DccFileTransfer > transfers        = new LinkedList < DccFileTransfer >();

    /**
     * @param lebot
     *            le bot
     */
    public DccManager(final IIrcBot lebot) {
        super();
        this.bot = lebot;
    }

    /**
     * @param dccFileTransfer
     * @return si le add a fonctionné.
     */
    public boolean add(final DccFileTransfer dccFileTransfer) {
        return this.transfers.add(dccFileTransfer);
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
        final StrTokenizer tokenizer = new StrTokenizer(request);
        final String temp1 = tokenizer.nextToken();
        final String type = temp1 + tokenizer.nextToken();
        final String filename = tokenizer.nextToken();
        if (type.equals(DCC_SEND)) {
            final long unsLongAddress = Long.parseLong(tokenizer.nextToken());
            final int unsignedShortPort = Integer.parseInt(tokenizer.nextToken());
            final long unsignedLongSize = Long.parseLong(tokenizer.nextToken());
            final DccFileTransfer transfer = new DccFileTransfer(this.bot, user, type, filename, unsLongAddress,
                    unsignedShortPort, unsignedLongSize);
            this.bot.onIncomingFileTransfer(transfer);
        } else if (type.equals(DCC_RESUME)) {
            final int port = Integer.parseInt(tokenizer.nextToken());
            final long progress = Long.parseLong(tokenizer.nextToken());
            synchronized (this) {
                for (final DccFileTransfer transfer : this.transfers) {
                    if (transfer.getUser().equals(user) && transfer.getPort() == port) {
                        transfer.resetProgress();
                        transfer.updateProgress(progress);
                        CtcpUtils.sendCtcpMsg(DCC_ACCEPT + SPC + "file.ext" + SPC + port + SPC + progress, user
                                .getNick(), ((AbstractIrcBot) this.bot).getOutputThread());
                        this.transfers.remove(transfer);
                        break;
                    }
                }
            }
        } else if (type.equals(DCC_ACCEPT)) {
            final int port = Integer.parseInt(tokenizer.nextToken());
            // long progress =
            Long.parseLong(tokenizer.nextToken());
            synchronized (this) {
                for (final DccFileTransfer transfer : this.transfers) {
                    if (transfer.getUser().equals(user) && transfer.getPort() == port) {
                        transfer.doReceive(transfer.getFile(), true);
                        this.transfers.remove(transfer);
                        break;
                    }
                }
            }
        } else if (type.equals(DCC_CHAT)) {
            final long address = Long.parseLong(tokenizer.nextToken());
            final int port = Integer.parseInt(tokenizer.nextToken());
            final DccChat chat = new DccChat(address, port);
            new MiniThread(this.bot, chat).execute();
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param dft
     * @return si on l'a bien retiré.
     */
    public boolean remove(final DccFileTransfer dft) {
        return this.transfers.remove(dft);
    }
}

/**
 * @author mauhiz
 */
class MiniThread extends AbstractRunnable {
    /**
     * mon bot.
     */
    private final IIrcBot bot;
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
    public MiniThread(final IIrcBot lebot, final DccChat chat1) {
        super();
        this.chat = chat1;
        this.bot = lebot;
    }

    /**
     * @see utils.AbstractRunnable#run()
     */
    @Override
    public void run() {
        this.bot.onIncomingChatRequest(this.chat);
    }
}
