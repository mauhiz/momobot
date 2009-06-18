package net.mauhiz.irc.bot.triggers;


/**
 * lol le nom de cette classe...
 * 
 * @author mauhiz
 */
public abstract class AbstractGourmandTrigger extends AbstractTextTrigger {
    
    /**
     * @param trigger
     */
    public AbstractGourmandTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.AbstractTextTrigger#isActivatedBy(java.lang.String)
     */
    @Override
    public boolean isActivatedBy(String msg) {
        return true;
    }
    
    /**
     * @param msg
     * @return si il s'agit bien du trigger.
     */
    public boolean isCommandMsg(String msg) {
        return super.isActivatedBy(msg);
    }
}
