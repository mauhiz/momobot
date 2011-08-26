package net.mauhiz.irc.base.data.rizon;

import net.mauhiz.irc.base.data.defaut.DefaultServer;
import net.mauhiz.irc.base.msg.NumericReplies;
import net.mauhiz.irc.base.msg.ServerMsg;

/**
 * @author mauhiz
 */
public class RizonServer extends DefaultServer {

    public RizonServer(String alias) {
        super(alias);
    }

    @Override
    public void handleSpecific(ServerMsg message, NumericReplies reply) {

        // TODO 42, 265, 266, 439 on Rizon
        switch (reply) {
            default:
                super.handleSpecific(message, reply);
        }
    }
}
