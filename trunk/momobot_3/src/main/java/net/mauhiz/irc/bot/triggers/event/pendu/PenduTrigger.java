package net.mauhiz.irc.bot.triggers.event.pendu;

import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Pendu;
import net.mauhiz.irc.bot.triggers.AbstractGourmandTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class PenduTrigger extends AbstractGourmandTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PenduTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        Channel chan = Channels.get(cme.getServer()).getChannel(cme.getTo());
        if (chan == null) {
            /* c est un msg prive */
            return;
        }
        ChannelEvent evt = chan.getEvt();
        if (isCommandMsg(cme.getMessage())) {
            String respMsg;
            if (evt != null) {
                respMsg = "Un " + evt.getClass().getSimpleName() + " est d�j� lanc� sur " + cme.getTo();
            } else {
                respMsg = "Devinez ce mot: " + new Pendu(chan).getDevinage();
            }
            Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
            control.sendMsg(resp);
        }
        if (evt instanceof Pendu) {
            final Pendu pendu = (Pendu) evt;
            String respMsg;
            if (cme.getMessage().length() == 1) {
                respMsg = pendu.submitLettre(cme.getMessage().toLowerCase(Locale.FRANCE).charAt(0)).toString();
            } else {
                respMsg = pendu.submitMot(cme.getMessage()).toString();
            }
            Privmsg resp = Privmsg.buildAnswer(cme, respMsg);
            control.sendMsg(resp);
            if (!pendu.isRunning()) {
                chan.setEvent(null);
            }
        }
    }
}
