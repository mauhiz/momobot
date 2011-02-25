package net.mauhiz.irc.bot.triggers.event.pendu;

import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Pendu;
import net.mauhiz.irc.bot.triggers.AbstractGourmandTrigger;

import org.apache.commons.lang.StringUtils;

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
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        IrcChannel chan = cme.getServer().findChannel(cme.getTo());
        if (chan == null) {
            /* c est un msg prive */
            return;
        }
        ChannelEvent evt = chan.getEvt();
        if (isCommandMsg(cme.getMessage())) {
            String respMsg;
            if (evt != null) {
                respMsg = "Un " + evt.getClass().getSimpleName() + " est deja lance sur " + cme.getTo();
            } else {
                respMsg = "Devinez ce mot: " + new Pendu(chan).getDevinage();
            }
            Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
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
                Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
                control.sendMsg(resp);
            }
            if (!pendu.isRunning()) {
                chan.stopEvent();
            }
        }
    }
}
