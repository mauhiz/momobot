package net.mauhiz.board.gui.remote;

import java.util.UUID;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;

public class MoveTrigger implements IPrivmsgTrigger {

    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        if (im instanceof CtcpMove) {
            String moveText = ((CtcpMove) im).getCtcpContent();
            String[] args = StringUtils.split(moveText, ' ');
            UUID gameId = UUID.fromString(args[0]);

        }
    }

    @Override
    public boolean isActivatedBy(String text) {
        return true;
    }

}
