package net.mauhiz.irc.bot.triggers.fun;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.math.RandomUtils;

/**
 * @author mauhiz
 */
public class MargueriteTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * 
     */
    private static final String[] MARGUERITE = {"un peu", "beaucoup", "passionément", "à la folie", "pas du tout",};
    
    /**
     * @param trigger
     */
    public MargueriteTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final String nom = getArgs(cme.getMessage());
        Privmsg msg = Privmsg.buildAnswer(cme, generateResponse(nom));
        control.sendMsg(msg);
    }
    
    /**
     * @param nom
     * @return un messsage
     */
    protected String generateResponse(final String nom) {
        return new StringBuilder(nom).append(" t'aime ").append(MARGUERITE[RandomUtils.nextInt(5)]).append('.')
                .toString();
    }
}
