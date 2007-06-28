package ircbot;

/**
 * @author mauhiz
 */
public interface IUserMode {
    /**
     * marks a users as invisible.
     */
    char U_INVISIBLE      = 'i';
    /**
     * operator flag.
     */
    char U_OPER           = 'o';
    /**
     * marks a user for receipt of server notices.
     */
    char U_SERVER_NOTICES = 's';
    /**
     * user receives wallops.
     */
    char U_WALLOPS        = 'w';
}
