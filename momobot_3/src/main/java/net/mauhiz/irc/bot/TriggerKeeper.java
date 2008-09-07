package net.mauhiz.irc.bot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.bot.triggers.ITrigger;

/**
 * @author mauhiz
 */
class TriggerKeeper implements Iterable<ITrigger> {
    /**
     * @author mauhiz
     */
    class UpdaterThread extends AbstractRunnable {
        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            synchronized (myLock) {
                if (!pendingRmvs.isEmpty()) {
                    holder.removeAll(pendingRmvs);
                    pendingRmvs.clear();
                }
                if (!pendingAdds.isEmpty()) {
                    holder.addAll(pendingAdds);
                    pendingAdds.clear();
                }
            }
        }
    }
    
    /**
     * serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * holds all the triggers.
     */
    final Set<ITrigger> holder = new HashSet<ITrigger>();
    
    /**
     * verrou
     */
    Object myLock = new Object();
    
    /**
     * to add
     */
    final Set<ITrigger> pendingAdds = new HashSet<ITrigger>();
    
    /**
     * to remove
     */
    final Set<ITrigger> pendingRmvs = new HashSet<ITrigger>();
    
    /**
     * @param e
     * @return if added
     */
    public boolean add(final ITrigger e) {
        if (holder.contains(e)) {
            return false;
        }
        return pendingAdds.add(e);
    }
    
    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<ITrigger> iterator() {
        synchronized (myLock) {
            return holder.iterator();
        }
    }
    
    /**
     * @param o
     * @return if removed
     */
    public boolean remove(final ITrigger o) {
        if (!holder.contains(o)) {
            return false;
        }
        return pendingRmvs.add(o);
    }
    
    /**
     * maj
     */
    void update() {
        new UpdaterThread().execute("Trigger Updater");
    }
}
