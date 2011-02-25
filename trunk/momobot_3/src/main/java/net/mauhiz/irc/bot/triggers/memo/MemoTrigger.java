package net.mauhiz.irc.bot.triggers.memo;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class MemoTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public MemoTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        String msg = getArgs(cme.getMessage());
        String respMsg;
        if (StringUtils.isEmpty(msg)) {
            respMsg = "Je connais " + MemoDb.getInstance(cme.getServer()).countMemos()
                    + " memos. $listmemos pour avoir une liste";
        } else {
            int index = msg.indexOf(' ');
            if (index < 1) {
                respMsg = MemoDb.getInstance(cme.getServer()).getMemo(msg);
            } else {
                String cle = msg.substring(0, index);
                respMsg = MemoDb.getInstance(cme.getServer()).setMemo(cle, msg.substring(index + 1));
            }
        }
        Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
        control.sendMsg(resp);
    }
}
