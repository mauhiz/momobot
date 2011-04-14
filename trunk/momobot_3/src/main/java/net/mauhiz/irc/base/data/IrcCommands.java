package net.mauhiz.irc.base.data;

public enum IrcCommands {
    /**
     * Command:     ADMIN
     * Parameters:     [<server>]
     */
    ADMIN,
    /**
     * Command:     CONNECT
     * Parameters:     <target server> [<port> [<remote server>]]
     */
    CONNECT,
    /**
     * Command:     ERROR
     * Parameters:     <error message>
     */
    ERROR,
    /**
     * Command:     INFO
     * Parameters:     [<server>] 
     */
    INFO,
    /**
     * Command:  INVITE
     * Parameters:     <nickname> <channel>
     */
    INVITE,
    /**
     * Command:     JOIN
     * Parameters:     <channel>{,<channel>} [<key>{,<key>}]
     */
    JOIN,
    /**
     * Command:     KICK
     * Parameters:     <channel> <user> [<comment>]
     */
    KICK,
    /**
     * Command:     KILL
     * Parameters:     <nickname> <comment>
     */
    KILL,
    /**
     * Command:     LINKS
     * Parameters:     [[<remote server>] <server mask>]
     */
    LINKS,
    /**
     * Command:     LIST
     * Parameters:     [<channel>{,<channel>} [<server>]]
     */
    LIST,
    /**
     * Command:     MODE
     * Parameters:  <channel> {[+|-]|o|p|s|i|t|n|b|v} [<limit>] [<user>] [<ban mask>]
     * Parameters:  <nickname> {[+|-]|i|w|s|o}
     */
    MODE,
    /**
     * Command:     NAMES
     * Parameters:     [<channel>{,<channel>}]
     */
    NAMES,
    /**
     * Command:     NICK
     * Parameters:     <nickname> [ <hopcount> ]
     */
    NICK,
    /**
     * Command:  NOTICE
     * Parameters:     <nickname> <text>
     */
    NOTICE,
    /**
     * Command:     OPER
     * Parameters:     <user> <password>
     */
    OPER,
    /**
     * Command:     PART
     * Parameters:     <channel>{,<channel>}
     */
    PART,
    /**
     * Command: PASS
     * Parameters: <password>
     */
    PASS,
    /**
     * Command:     PING
     * Parameters:     <server1> [<server2>]
     */
    PING,
    /**
     * Command:     PONG
     * Parameters:     <daemon> [<daemon2>]
     */
    PONG,
    /**
     * Command:     PRIVMSG
     * Parameters:     <receiver>{,<receiver>} <text to be sent>
     */
    PRIVMSG,
    /**
      * Command:     QUIT
      * Parameters:     [<Quit message>]
      */
    QUIT,
    /**
     * Command:     SERVER
     * Parameters:     <servername> <hopcount> <info>
     */
    SERVER,
    /**
     * Command:     SQUIT
     * Parameters:     <server> <comment>
     */
    SQUIT,
    /**
     * Command:     STATS
     * Parameters:     [<query> [<server>]]
     */
    STATS,
    /**
     * Command:     TIME
     * Parameters:     [<server>]
     */
    TIME,
    /**
     * Command:     TOPIC
     * Parameters:     <channel> [<topic>]
     */
    TOPIC,
    /**
     * Command:     TRACE
     * Parameters:     [<server>]
     */
    TRACE,
    /**
     * Command:     USER
     * Parameters:     <username> <hostname> <servername> <realname>
     */
    USER,
    /**
     * Command:     VERSION
     * Parameters:     [<server>]
     */
    VERSION,
    /**
     * Command:     WHO
     * Parameters:     [<name> [<o>]]
     */
    WHO,
    /**
     * Command:     WHOIS
     * Parameters:     [<server>] <nickmask>[,<nickmask>[,...]]
     */
    WHOIS,
    /**
     * Command:     WHOWAS
     * Parameters:     <nickname> [<count> [<server>]]
     */
    WHOWAS;
}
