package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 * @param <C>
 *            a quelle classe se hooker?
 */
public abstract class AbstractHook<C extends IHookable<?>> implements IHook<C> {
    protected C hook;
    
    /**
     * @param hook1
     */
    public AbstractHook(final C hook1) {
        hook = hook1;
        hook.hookObject(this);
    }
}
