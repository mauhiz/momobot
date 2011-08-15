package net.mauhiz.irc.bot.triggers.event.pendu;

import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.IChannelEvent;
import net.mauhiz.irc.bot.event.Pendu;
import net.mauhiz.irc.bot.triggers.AbstractGourmandTrigger;

import org.apache.commons.lang3.StringUtils;

/**
 * @author mauhiz
 */
public class PenduTrigger extends AbstractGourmandTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PenduTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    public void doTrigger(Privmsg cme, IIrcControl control) {
        if (cme.isToChannel()) {
            IrcChannel chan = (IrcChannel) cme.getTo();
            IChannelEvent evt = chan.getEvt();
            if (isCommandMsg(cme.getMessage())) {
                String respMsg;
                if (evt == null) {
                    respMsg = "Devinez ce mot: " + new Pendu(chan).getDevinage();
                } else {
                    respMsg = "Un " + evt.getClass().getSimpleName() + " est deja lance sur " + cme.getTo();
                }
                Privmsg resp = new Privmsg(cme, respMsg);
                control.sendMsg(resp);
            }
            if (evt instanceof Pendu) {
                Pendu pendu = (Pendu) evt;
                String respMsg;
                if (cme.getMessage().length() == 1) {
                    respMsg = pendu.submitLettre(cme.getMessage().toLowerCase(Locale.FRANCE).charAt(0));
                } else {
                    respMsg = pendu.submitMot(cme.getMessage()).toString();
                }
                if (StringUtils.isNotBlank(respMsg)) {
                    Privmsg resp = new Privmsg(cme, respMsg);
                    control.sendMsg(resp);
                }
                if (!pendu.isRunning()) {
                    chan.stopEvent();
                }
            }
        }
    }
}
