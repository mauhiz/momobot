package momobot.trigger.math;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * Calcule un sha-256.
 * 
 * @author mauhiz
 */
public class Sha256Trigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * 
     */
    static final Logger LOG = Logger.getLogger(Sha256Trigger.class);

    /**
     * @param input
     * @return le md5
     * @throws NoSuchAlgorithmException
     */
    static String computeSha256(final byte[] input) throws NoSuchAlgorithmException {
        byte[] output = MessageDigest.getInstance("SHA-256").digest(input);
        StrBuilder sb = new StrBuilder();
        for (byte b : output) {
            sb.append(Md5Trigger.byteToString(b));
        }
        return sb.toString();
    }

    /**
     * @param trigger
     */
    public Sha256Trigger(final String trigger) {
        super(trigger);
    }

    /**
     * MessageDigest
     * 
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        final String args = getArgs(message);
        if (StringUtils.isEmpty(args)) {
            MomoBot.getBotInstance().sendMessage(channel, "sha-256 de quoi ?");
        } else {
            try {
                MomoBot.getBotInstance().sendMessage(channel,
                        "sha-256 de " + args + ": " + computeSha256(args.getBytes()));
            } catch (final NoSuchAlgorithmException nsae) {
                MomoBot.getBotInstance().sendMessage(channel, "J'ai pas de sha-256. Sry.");
            }
        }
    }
}
