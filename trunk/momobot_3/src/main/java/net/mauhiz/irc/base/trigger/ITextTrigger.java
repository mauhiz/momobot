package net.mauhiz.irc.base.trigger;

/**
 * @author mauhiz
 */
public interface ITextTrigger extends ITrigger {

    /**
     * @param text
     * @return si le test est concluant.
     */
    boolean isActivatedBy(String text);
}
