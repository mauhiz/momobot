package net.mauhiz.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @param <S>
 *            esse
 * 
 * @author mauhiz
 */
public class Hooks<S> {
    /**
     * key = esse<br>
     * value = hooks associes
     */
    private static final Map<Object, Hooks<?>> HOOKS_HOLDER = new HashMap<Object, Hooks<?>>();
    
    /**
     * @param <S>
     * @param <J>
     *            jambon
     * @param esse
     * @param jambon
     */
    public static <S, J> void addHook(S esse, J jambon) {
        Hooks<S> hTable = getHooks(esse);
        
        if (hTable == null) {
            hTable = new Hooks<S>(esse);
        }
        
        hTable.addHook(jambon);
    }
    
    /**
     * @param <J>
     * @param <S>
     * @param esse
     * @param hookClass
     * @return jambon
     */
    public static <J, S> J getHook(S esse, Class<J> hookClass) {
        Hooks<S> hTable = getHooks(esse);
        if (hTable == null) {
            return null;
        }
        
        return hTable.getHook(hookClass);
    }
    
    /**
     * @param <S>
     * @param esse
     * @return hooks
     */
    public static <S> Hooks<S> getHooks(S esse) {
        return (Hooks<S>) HOOKS_HOLDER.get(esse);
    }
    
    /**
     * @param <J>
     * @param jambon
     */
    public static <J> void remove(J jambon) {
        Class<?> jClass = jambon.getClass();
        for (Iterator<Hooks<?>> iHooks = HOOKS_HOLDER.values().iterator(); iHooks.hasNext();) { // parcours des hooks
            Hooks<?> hs = iHooks.next();
            for (Iterator<Class<?>> iClasses = hs.jambons.keySet().iterator(); iClasses.hasNext();) {
                Class<?> iClass = iClasses.next();
                if (jClass.equals(iClass)) {
                    iClasses.remove();
                }
            }
            if (hs.jambons.isEmpty()) { // plus rien d accroche
                iHooks.remove();
            }
        }
        
    }
    
    /**
     * key = class of jambon<br>
     * value = jambon itself
     */
    private final Map<Class<?>, Object> jambons = new HashMap<Class<?>, Object>();
    
    /**
     * @param esse
     */
    public Hooks(S esse) {
        HOOKS_HOLDER.put(esse, this);
    }
    
    public <J> void addHook(J jambon) {
        jambons.put(jambon.getClass(), jambon);
    }
    
    /**
     * @param <J>
     * @param hookClass
     * @return hooked object
     */
    public <J> J getHook(Class<J> hookClass) {
        Object jambon = jambons.get(hookClass);
        if (jambon == null) {
            for (Object value : jambons.values()) {
                if (hookClass.isAssignableFrom(value.getClass())) {
                    jambon = value;
                    break;
                }
            }
        }
        
        return (J) jambon;
    }
}
