package ircbot;

/**
 * @author viper
 */
public abstract class ACtcp implements ICtcpCommands, IIrcCommands {

    /**
     * @param toQuote
     *            le message à quoter
     * @return une commande CTCP
     */
    public static String ctcpQuote(final String toQuote) {
        return STX + toQuote + STX;
    }

    /**
     * @param msg
     *            le message à envoyer
     * @param recipient
     *            le destinataire (chan, user..)
     * @param out
     *            le moyen d'expédition
     */
    public static void sendAction(final String msg, final String recipient,
            final OutputQueue out) {
        sendCtcpMsg(E_ACTION + SPC + msg, recipient, out);
    }

    /**
     * @param msg
     *            le message à envoyer
     * @param recipient
     *            le destinataire (chan, user..)
     * @param out
     *            le moyen d'expédition
     */
    public static void sendCtcpMsg(final String msg, final String recipient,
            final OutputQueue out) {
        out.put(PRIVMSG + SPC + recipient + SPC + ':' + ctcpQuote(msg));
    }

    /**
     * @param msg
     *            le message à envoyer
     * @param recipient
     *            le destinataire (chan, user..)
     * @param out
     *            le moyen d'expédition
     */
    public static void sendCtcpNotice(final String msg, final String recipient,
            final OutputQueue out) {
        out.put(NOTICE + SPC + recipient + SPC + ':' + ctcpQuote(msg));
    }

    /**
     * constructeur privé.
     */
    protected ACtcp() {
        throw new UnsupportedOperationException();
    }
}
