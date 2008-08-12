package net.mauhiz.irc.bot.triggers.dispo;

/**
 * @author mauhiz
 */
public class Dispo {
    /**
     * La première heure (21h00).
     */
    private final int    heure1;
    /**
     * La deuxième heure (22h30).
     */
    private final int    heure2;
    /**
     * L'auth Qnet.
     */
    private final String qauth;

    /**
     * @param qauth1
     *            l'auth Qnet
     * @param heure11
     *            l'heure1
     * @param heure21
     *            l'heure2
     */
    public Dispo(final String qauth1, final int heure11, final int heure21) {
        this.qauth = qauth1;
        this.heure1 = heure11;
        this.heure2 = heure21;
    }

    /**
     * @return Returns the heure1.
     */
    public final int getHeure1() {
        return this.heure1;
    }

    /**
     * @return Returns the heure2.
     */
    public final int getHeure2() {
        return this.heure2;
    }

    /**
     * @return Returns the qauth.
     */
    public final String getQauth() {
        return this.qauth;
    }
}
