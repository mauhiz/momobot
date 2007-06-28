package momobot.trigger.fun;

import org.apache.commons.lang.text.StrTokenizer;

import momobot.MomoBot;
import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

/**
 * @author mauhiz
 */
public class BaseConvTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param user
     * @param base
     * @return si la base est acceptable
     */
    private static boolean checkBase(final IrcUser user, final int base) {
        if (base < Character.MIN_RADIX) {
            MomoBot.getBotInstance().sendNotice(user, "La base " + base + " est trop petite");
            return false;
        } else if (base > Character.MAX_RADIX) {
            return false;
        }
        return true;
    }

    /**
     * @param trigger
     */
    public BaseConvTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @Override
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        final String args = getArgs(message);
        if (args.trim().isEmpty()) {
            showUsage(user);
            return;
        }
        final StrTokenizer tok = new StrTokenizer(args);
        final String nombre = tok.nextToken();
        final int base1 = Integer.parseInt(tok.nextToken());
        if (!checkBase(user, base1)) {
            return;
        }
        try {
            final int intNombre = Integer.parseInt(nombre, base1);
            final int base2 = Integer.parseInt(tok.nextToken());
            if (!checkBase(user, base2)) {
                return;
            }
            final String resultat = Integer.toString(intNombre, base2);
            MomoBot.getBotInstance().sendMessage(channel,
                    nombre + " en base " + base1 + " = " + resultat + " en base " + base2);
        } catch (final NumberFormatException nfe) {
            MomoBot.getBotInstance().sendNotice(user, "Le nombre " + nombre + " est illisible en base " + base1);
            return;
        }
    }

    /**
     * @param user
     */
    private void showUsage(final IrcUser user) {
        MomoBot.getBotInstance().sendNotice(user, "Syntaxe : " + getTriggerText() + " nombre base_from base_to");
        MomoBot.getBotInstance().sendNotice(user, "Exemple : " + getTriggerText() + " ff 16 10");
    }
}
