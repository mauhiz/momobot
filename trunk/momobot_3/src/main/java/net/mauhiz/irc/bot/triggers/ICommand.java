package net.mauhiz.irc.bot.triggers;

public interface ICommand {

    /**
     * @return Un message d'aide; par defaut, le texte du trigger.
     */
    String getTriggerHelp();

    /**
     * @return trigger text
     */
    String getTriggerText();
}
