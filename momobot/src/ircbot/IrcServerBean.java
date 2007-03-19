package ircbot;

import java.net.InetSocketAddress;

/**
 * @author viper
 */
public class IrcServerBean {
    /**
     * port par défaut.
     */
    private static final int DEFAULT_IRC_PORT = 6667;
    /**
     * Hôte.
     */
    private String           hostname         = "";
    /**
     * Mot de passe.
     */
    private String           password         = "";
    /**
     * port.
     */
    private int              port             = DEFAULT_IRC_PORT;

    /**
     * @param hostname1
     *            le nom d'hôte
     */
    public IrcServerBean(final String hostname1) {
        this.hostname = hostname1;
    }

    /**
     * @param hostname1
     *            le nom d'hôte
     * @param port2
     *            le port
     */
    public IrcServerBean(final String hostname1, final int port2) {
        this(hostname1);
        this.port = port2;
    }

    /**
     * @param hostname1
     *            le nom d'hôte
     * @param port1
     *            le port
     * @param password1
     *            le mot de passe
     */
    public IrcServerBean(final String hostname1, final int port1,
            final String password1) {
        this(hostname1, port1);
        this.password = password1;
    }

    /**
     * @return l'adresse
     */
    final InetSocketAddress getAddress() {
        return new InetSocketAddress(this.hostname, this.port);
    }

    /**
     * @return le password
     */
    final String getPassword() {
        return this.password;
    }
}
