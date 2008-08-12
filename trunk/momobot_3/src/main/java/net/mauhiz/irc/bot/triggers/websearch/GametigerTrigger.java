package net.mauhiz.irc.bot.triggers.websearch;

import java.util.List;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class GametigerTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GametigerTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final WebQuery query = new WebQuery("gametiger", getArgs(cme.getMessage()));
        final List<String> resultats = query.results();
        if (resultats.isEmpty()) {
            Notice notice = Notice.buildPrivateAnswer(cme, "rien trouvé :/");
            control.sendMsg(notice);
            return;
        }
        for (final String next : resultats) {
            Notice notice = Notice.buildPrivateAnswer(cme, next);
            control.sendMsg(notice);
        }
    }
}
