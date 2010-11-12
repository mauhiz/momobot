package net.mauhiz.irc.bot.triggers.event.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.fake.FakeUser;
import net.mauhiz.irc.bot.event.Team;

/**
 * @author topper
 */
public class TournamentTeam extends Team {
    /**
     * 
     */
    private Locale country;
    /**
     * 
     */
    private final int id;
    /**
     * 
     */
    private final IrcUser owner;
    
    /**
     * @param size1
     * @param id1
     * @param nom1
     * @param country1
     * @param ircuser
     */
    public TournamentTeam(int size1, int id1, String nom1, Locale country1, IrcUser ircuser) {
        super(size1, nom1);
        country = country1;
        id = id1;
        owner = ircuser;
    }
    
    public boolean add(String ele) {
        return super.add(new FakeUser(ele));
    }
    
    public boolean addAll(String[] elems) {
        List<IrcUser> fakes = new ArrayList<IrcUser>(elems.length);
        for (String elem : elems) {
            fakes.add(new FakeUser(elem));
        }
        return super.addAll(fakes);
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
    public int getId() {
        return id;
    }
    
    /**
     * @param ircuser
     * @return if ircuser is Owner
     * 
     */
    public boolean isOwner(IrcUser ircuser) {
        return owner.equals(ircuser);
    }
    
    /**
     * @param country1
     */
    public void setCountry(Locale country1) {
        country = country1;
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.Team#toString()
     */
    @Override
    public String toString() {
        StringBuilder listPlayer = new StringBuilder();
        for (IrcUser element : this) {
            listPlayer.append(element).append(' ');
        }
        
        return "Team #" + id + " Tag :" + getNom() + " Pays :" + country.getCountry() + " Player(s) :" + listPlayer;
    }
}
