package net.mauhiz.irc.base.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mauhiz
 * @param <C>
 */
public abstract class AbstractHookable<C extends IHookable<?>> implements IHookable<C> {
    protected final Map<Class<? extends IHook<C>>, IHook<C>> hooks = new HashMap<Class<? extends IHook<C>>, IHook<C>>();
    /**
     * @see net.mauhiz.irc.base.data.IHookable#getHookedObject(java.lang.Class)
     */
    @Override
    public <H extends IHook<C>> H getHookedObject(final Class<H> hookClass) {
        return (H) hooks.get(hookClass);
    }
    
    /**
     * @param <H>
     * @param hookObject
     */
    public <H extends IHook<C>> void hookObject(final H hookObject) {
        hooks.put((Class<? extends AbstractHook<C>>) hookObject.getClass(), (IHook<C>) hookObject);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IHookable#removeHook(java.lang.Class)
     */
    @Override
    public <H extends IHook<C>> void removeHook(final Class<H> hookClass) {
        hooks.remove(hookClass);
    }
}
