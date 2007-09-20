package ircbot.trigger;


/**
 * @author mauhiz
 */
public interface ITrigger {
    /**
     * @return Un message d'aide; par défaut, le texte du trigger.
     */
    String getTriggerText();
    
    /**
     * @param msg
     * @return le résultat du test
     */
    boolean isActivatedBy(final String msg);
}
