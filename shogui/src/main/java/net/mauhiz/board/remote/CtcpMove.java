package net.mauhiz.board.remote;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.Ctcp;

public class CtcpMove extends Ctcp {

    public CtcpMove(IIrcServerPeer server, Target from, Target to, String message) {
        super(server, from, to, message);
    }

    @Override
    protected String getCommand() {
        return "MOVE";
    }

}
