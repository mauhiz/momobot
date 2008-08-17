package net.mauhiz.irc.bot.cs;

/**
 * @author mauhiz
 * 
 */
public interface ServerFlags {
    /**
     * indique que le serveur est dédié.
     */
    char DEDICATED = 'd';
    /**
     * moteur de HL 1.
     */
    char GOLDSOURCE = 'm';
    /**
     * indique que le serveur est un hltv.
     */
    char HLTV = 'p';
    /**
     * indique que le serveur est sous linux.
     */
    char LINUX = 'l';
    /**
     * indique que le serveur est listen.
     */
    char LISTEN = 'l';
    /**
     * moteur source.
     */
    char SOURCE = 'I';
    /**
     * indique que le serveur est sous windows.
     */
    char WINDOWS = 'w';
    
}
