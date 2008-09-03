package net.mauhiz.irc.bot.triggers.memo;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class MemoTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public MemoTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final String msg = getArgs(cme.getMessage());
        String respMsg;
        if (StringUtils.isEmpty(msg)) {
            respMsg = "Je connais " + MemoDb.getInstance(cme.getServer()).countMemos()
                    + " memos. $listmemos pour avoir une liste";
        } else {
            final int index = msg.indexOf(' ');
            if (index < 1) {
                respMsg = MemoDb.getInstance(cme.getServer()).getMemo(msg);
            } else {
                final String cle = msg.substring(0, index);
                respMsg = MemoDb.getInstance(cme.getServer()).setMemo(cle, msg.substring(index + 1));
            }
        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
