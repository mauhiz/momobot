package momobot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPriveTrigger;
import ircbot.trigger.IPublicTrigger;

import java.util.Set;
import java.util.TreeSet;

import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

/**
 * @author mauhiz
 */
public class BadWordTrigger extends AbstractTrigger implements IPublicTrigger, IPriveTrigger {
    /**
     * TODO utiliser SQL.
     */
    private static final Set<String> BADWORDS = new TreeSet<String>();
    static {
        BADWORDS.add("caca");
        BADWORDS.add("prout");
    }

    /**
     * @param trigger
     */
    public BadWordTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * TODO mécanisme de sauvegarde.
     * 
     * @see IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    public void executePrivateTrigger(final IrcUser user, final String message) {
        if (!user.isAdmin()) {
            return;
        }
        final String badword = getArgs(message);
        if (StringUtils.isEmpty(badword)) {
            MomoBot.getBotInstance().sendMessage(user, "Syntaxe: " + getTriggerText() + " badword");
        } else {
            BADWORDS.add(badword);
            MomoBot.getBotInstance().sendMessage(user, "Badword ajouté: " + badword);
        }
    }

    /**
     * @see IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        for (final String word : new StrTokenizer(getArgs(message)).getTokenArray()) {
            for (final String badword : BADWORDS) {
                if (badword.equalsIgnoreCase(word)) {
                    getAngry(badword, user, channel);
                }
            }
        }
    }

    /**
     * @param badword
     * @param user
     * @param channel
     */
    private void getAngry(final String badword, final IrcUser user, final Channel channel) {
        if (channel.hasOp(MomoBot.getBotInstance().getNick())) {
            /* hinhin, je suis op */
            MomoBot.getBotInstance().kick(channel, user, "You shall not use bad words such as " + badword);
        } else {
            /* je rage quand même */
            MomoBot.getBotInstance().sendMessage(channel, user + " is so rude, he said " + badword + "!!");
        }
    }
}
