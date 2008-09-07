package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 * @param <C>
 */
public interface IHookable<C extends IHookable<?>> {
    /**
     * @param <H>
     * @param hookClass
     * @return
     */
    <H extends IHook<C>> H getHookedObject(Class<H> hookClass);
    
    /**
     * @param <H>
     * @param hookObject
     */
    <H extends IHook<C>> void hookObject(final H hookObject);
    
    /**
     * @param <H>
     * @param hookClass
     */
    <H extends IHook<C>> void removeHook(Class<H> hookClass);
}
