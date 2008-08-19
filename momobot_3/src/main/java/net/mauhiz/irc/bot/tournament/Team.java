package net.mauhiz.irc.bot.tournament;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author topper
 */
public class Team extends ArrayList<String> {
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
    private Locale country;
    /**
     * 
     */
    private int id;
    /**
     * 
     */
    private String nom;
    
    /**
     * @param size1
     * @param nom1
     * @param country1
     * 
     */
    public Team(final int size1, final int id1, final String nom1, final Locale country1) {
        super(size1);
        capacity = size1;
        nom = nom1;
        country = country1;
        id = id1;
    }
    
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
     * @return {@link #capacity}.
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * @return country
     */
    public Locale getCountry() {
        return country;
    }
    
    /**
     * @return id de la team
     */
    public final int getId() {
        return id;
    }
    
    /**
     * 
     * 
     **/
    public String getNom() {
        return nom;
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
     * 
     */
    public void setCountry(final Locale country1) {
        country = country1;
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
        String listPlayer = "";
        if (!isEmpty()) {
            
            for (String element : this) {
                listPlayer += element + " ";
            }
        }
        return "Team n°" + id + " Tag :" + nom + " Pays :" + country + " Player(s) :" + listPlayer;
        
    }
}
