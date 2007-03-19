package momobot;

/**
 * @author Administrator
 */
public class Dispo {
    /**
     * La première heure (21h00).
     */
    private int    heure1 = 0;

    /**
     * La deuxième heure (22h30).
     */
    private int    heure2 = 0;

    /**
     * L'auth Qnet.
     */
    private String qauth  = "";

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
