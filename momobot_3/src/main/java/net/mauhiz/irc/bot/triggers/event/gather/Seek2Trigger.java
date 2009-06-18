package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.SeekWar2;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * @author Topper
 */
public class Seek2Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public Seek2Trigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        IrcServer server = im.getServer();
        IrcChannel chan = server.findChannel(im.getTo());
        ChannelEvent event = chan.getEvt();
        if (event == null) {
            String[] args = StringUtils.split(getArgs(im.getMessage()));
            int nbPlayers;
            try {
                nbPlayers = Integer.parseInt(args[0]);
                if (nbPlayers < 0) {
                    nbPlayers = 5;
                    throw new NumberFormatException("Integer <0");
                }
                
            } catch (NumberFormatException e) {
                Privmsg resp = Privmsg.buildAnswer(im, getTriggerHelp());
                control.sendMsg(resp);
                return;
            }
            SeekWar2 skwar = new SeekWar2(chan, control, server, nbPlayers, im.getMessage().substring(
                    args[0].length() - 1, im.getMessage().length() - 1));
            Privmsg resp = Privmsg.buildAnswer(im, skwar.toString());
            control.sendMsg(resp);
        } else if (event instanceof Gather) {
            Gather gather = (Gather) event;
            SeekWar2 skwar = new SeekWar2(chan, control, server, gather.getNumberPlayers(), getArgs(im.getMessage()));
            Privmsg resp = Privmsg.buildAnswer(im, skwar.toString());
            control.sendMsg(resp);
            
        } else {
            String msg = "Un " + event.getClass().getSimpleName() + " est deja lance sur " + im.getTo();
            Privmsg resp = Privmsg.buildAnswer(im, msg);
            control.sendMsg(resp);
        }
    }
    
    @Override
    public String getTriggerHelp() {
        return "Syntaxe: " + getTriggerText() + " [n (n>1)] [lvl]";
    }
}
