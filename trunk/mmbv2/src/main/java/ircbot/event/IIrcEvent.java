package ircbot.event;

/**
 * @author mauhiz
 */
public interface IIrcEvent {
    /**
     * @return le contenu brut du message provenant du serveur
     */
    String getRaw();
}
