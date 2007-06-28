package ircbot;

/**
 * @author mauhiz
 */
public interface IIrcCommands extends IIrcSpecialChars {
    /**
     * .
     */
    String INVITE  = "INVITE";
    /**
     * .
     */
    String JOIN    = "JOIN";
    /**
     * .
     */
    String KICK    = "KICK";
    /**
     * .
     */
    String LIST    = "LIST";
    /**
     * .
     */
    String MODE    = "MODE";
    /**
     * .
     */
    String NICK    = "NICK";
    /**
     * .
     */
    String NOTICE  = "NOTICE";
    /**
     * .
     */
    String PART    = "PART";
    /**
     * .
     */
    String PASS    = "PASS";
    /**
     * .
     */
    String PING    = "PING";
    /**
     * .
     */
    String PONG    = "PONG";
    /**
     * .
     */
    String PRIVMSG = "PRIVMSG";
    /**
     * .
     */
    String QUIT    = "QUIT";
    /**
     * .
     */
    String TOPIC   = "TOPIC";
    /**
     * .
     */
    String USER    = "USER";
    /**
     * .
     */
    String WHOIS   = "WHOIS";
}
