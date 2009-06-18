package net.mauhiz.irc.bot.triggers.cs;

/**
 * Bean contenant...
 * <dl>
 * <dd>nom</dd>
 * <dt>String</dt>
 * <dd>steamId</dd>
 * <dt>String</dt>
 * <dd>frags</dd>
 * <dt>int</dt>
 * <dd>master</dd>
 * <dt>boolean</dt>
 * </dl>
 * 
 * @author mauhiz
 */
public class Player {
    /**
     * nombre de frags.
     */
    private int frags;
    /**
     * si le joueur a du pouvoir sur le bot.
     */
    private boolean master;
    /**
     * nom.
     */
    private String nom;
    /**
     * steam ID.
     */
    private String steamId;
    
    /**
     * @param steamid
     *            le steam ID
     */
    public Player(String steamid) {
        steamId = steamid;
    }
    
    /**
     * @return Returns the frags.
     */
    public int getFrags() {
        return frags;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return nom;
    }
    
    /**
     * @return the steam_id
     */
    public String getSteamId() {
        return steamId;
    }
    
    /**
     * @return the master
     */
    public boolean isMaster() {
        return master;
    }
    
    /**
     * @param frags1
     *            The frags to set.
     */
    public void setFrags(int frags1) {
        frags = frags1;
    }
    
    /**
     * @param master1
     *            the master to set
     */
    public void setMaster(boolean master1) {
        master = master1;
    }
    
    /**
     * @param nom1
     *            The name to set.
     */
    public void setName(String nom1) {
        nom = nom1;
    }
    
    /**
     * @param steamId1
     *            the steam_id to set
     */
    public void setSteamId(String steamId1) {
        steamId = steamId1;
    }
}
