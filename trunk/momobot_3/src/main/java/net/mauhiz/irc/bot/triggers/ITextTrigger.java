package net.mauhiz.irc.bot.triggers;

/**
 * @author mauhiz
 */
public interface ITextTrigger extends ITrigger {
    
    /**
     * @return Un message d'aide; par défaut, le texte du trigger.
     */
    String getTriggerHelp();
    
    /**
     * @return trigger text
     */
    String getTriggerText();
    
    /**
     * @param text
     * @return si le test est concluant.
     */
    boolean isActivatedBy(String text);
}
