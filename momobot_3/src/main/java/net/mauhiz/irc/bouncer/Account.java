package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.data.IrcServer;

public class Account {
    String                          password;
    transient BouncerTriggerManager relatedManager = new BouncerTriggerManager();
    IrcServer                       server;
    long                            startTime;
    String                          username;
}
