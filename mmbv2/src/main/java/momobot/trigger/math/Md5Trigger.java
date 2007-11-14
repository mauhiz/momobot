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
 * Calcule un md5.
 * 
 * @author mauhiz
 */
public class Md5Trigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * 
     */
    static final Logger LOG = Logger.getLogger(Md5Trigger.class);

    /**
     * @param b
     * @return un affichage hexa du byte non signe
     */
    static String byteToString(final byte b) {
        String out = Integer.toHexString(b);
        if (out.length() > 2) {
            return out.substring(6);
        } else if (out.length() == 1) {
            return "0" + out;
        }
        return out;
    }

    /**
     * @param input
     * @return le md5
     * @throws NoSuchAlgorithmException
     */
    static String computeMd5(final byte[] input) throws NoSuchAlgorithmException {
        byte[] output = MessageDigest.getInstance("MD5").digest(input);
        StrBuilder sb = new StrBuilder();
        for (byte b : output) {
            sb.append(byteToString(b));
        }
        return sb.toString();
    }

    /**
     * @param trigger
     */
    public Md5Trigger(final String trigger) {
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
            MomoBot.getBotInstance().sendMessage(channel, "md5 de quoi ?");
        } else {
            try {
                MomoBot.getBotInstance().sendMessage(channel, "md5 de " + args + ": " + computeMd5(args.getBytes()));
            } catch (final NoSuchAlgorithmException nsae) {
                MomoBot.getBotInstance().sendMessage(channel, "J'ai pas de md5. Sry.");
            }
        }
    }
}
