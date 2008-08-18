package net.mauhiz.irc.bot.tournament;

import java.util.ArrayList;

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
    private String country;
    /**
     * 
     */
    private int ID;
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
    public Team(final int size1, final int id1, final String nom1, final String country1) {
        super(size1);
        capacity = size1;
        nom = nom1;
        country = country1;
        ID = id1;
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
    public String getCountry() {
        return country;
    }
    
    /**
     * @return id de la team
     */
    public final int getId() {
        return ID;
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
    public void setCountry(final String country1) {
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
        return "Team n°" + ID + " Tag :" + nom + " Pays :" + country + " Player(s) :" + listPlayer;
        
    }
}
