package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author mauhiz
 */
public class Team extends ArrayList<IrcUser> {
    private final int capacity;
    private String nom;
    
    /**
     * @param size1
     * @param nom1
     */
    public Team(int size1, String nom1) {
        super(size1);
        capacity = size1;
        nom = nom1;
    }
    
    /**
     * @see java.util.ArrayList#add(java.lang.Object)
     */
    @Override
    public boolean add(IrcUser a) {
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
     * @return {@link #nom}.
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * @return si la team est complete.
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
    public void setNom(String nom1) {
        nom = nom1;
    }
    
    /**
     * debug only
     * 
     * @see java.util.AbstractCollection#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
