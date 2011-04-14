package net.mauhiz.board.gui.remote;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.Ctcp;

public class CtcpMove extends Ctcp {

    public CtcpMove(Target from, Target to, IIrcServerPeer server, String message) {
        super(from, to, server, message);
    }

    @Override
    protected String getCommand() {
        return "MOVE";
    }

}
