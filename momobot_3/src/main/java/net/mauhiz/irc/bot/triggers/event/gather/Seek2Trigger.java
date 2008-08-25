package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.SeekWar2;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @param trigger
 *            le trigger
 */
public class Seek2Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public Seek2Trigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        IrcServer server = im.getServer();
        Channel chan = Channels.getInstance(server).get(im.getTo());
        ChannelEvent event = chan.getEvt();
        if (event != null) {
            String msg = "Un " + event.getClass().getSimpleName() + " est déjà lancé sur " + im.getTo();
            Privmsg resp = Privmsg.buildAnswer(im, msg);
            control.sendMsg(resp);
            return;
        } else {
            String[] args = StringUtils.split(getArgs(im.getMessage()));
            int nbPlayers = 5;
            String respMsg;
            try {
                nbPlayers = Integer.parseInt(args[0]);
                if (nbPlayers < 0) {
                    nbPlayers = 5;
                    throw new NumberFormatException("Integer <0");
                }
                
            } catch (NumberFormatException e) {
                respMsg = "Erreur : L'argument qui suit doit etre un chiffre superieur a 1";
                Privmsg resp = Privmsg.buildAnswer(im, respMsg);
                control.sendMsg(resp);
                return;
            }
            
            SeekWar2 skwar = new SeekWar2(chan, control, server, nbPlayers, im.getMessage().substring(
                    args[0].length() - 1, im.getMessage().length() - 1));
            Privmsg resp = Privmsg.buildAnswer(im, skwar.toString());
            control.sendMsg(resp);
        }
    }
}
