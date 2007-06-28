package momobot.event;

import ircbot.IrcUser;

import java.util.ArrayList;

/**
 * @author mauhiz
 */
public class Team extends ArrayList < IrcUser > {
    /**
     * 
     */
    private static final long serialVersionUID = 1;
    /**
     * 
     */
    private final int         capacity;
    /**
     * 
     */
    private String            nom;

    /**
     * @param size1
     * @param nom1
     */
    public Team(final int size1, final String nom1) {
        super(size1);
        this.capacity = size1;
        this.nom = nom1;
    }

    /**
     * @return {@link #capacity}.
     */
    public int getCapacity() {
        return this.capacity;
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
        return this.capacity - size();
    }

    /**
     * @param nom1
     */
    public void setNom(final String nom1) {
        this.nom = nom1;
    }

    /**
     * @return {@link #nom}.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return this.nom;
    }
}
