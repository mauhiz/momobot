package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class Team extends ArrayList<IrcUser> {
    /**
     * 
     */
    private static final long serialVersionUID = 1;
    /**
     * 
     */
    private final int capacity;
    /**
     * 
     */
    private String nom;
    
    /**
     * @param size1
     * @param nom1
     */
    public Team(final int size1, final String nom1) {
        super(size1);
        capacity = size1;
        nom = nom1;
    }
    
    /**
     * @see java.util.ArrayList#add(java.lang.Object)
     */
    @Override
    public boolean add(final IrcUser a) {
        if (isFull()) {
            throw new IllegalStateException("Team is full!");
        }
        return super.add(a);
    }
    
    /**
     * @return {@link #capacity}.
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * @return si la team est complète.
     */
    public boolean isFull() {
        return remainingPlaces() <= 0;
    }
    
    /**
     * @return le nombre de places restantes.
     */
    public int remainingPlaces() {
        return capacity - size();
    }
    
    /**
     * @param nom1
     */
    public void setNom(final String nom1) {
        nom = nom1;
    }
    
    /**
     * @return {@link #nom}.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return nom;
    }
}
