package ircbot;

import org.apache.commons.lang.StringUtils;

/**
 * Les utilisateurs sont rangés dans une Map (nickLowerCase, object) permettant un accès rapide lorsqu'un évènement
 * associé à un utilisateur apparaît.
 * @author mauhiz
 */
public class QnetUser extends IrcUser {
    /**
     * @param nick
     *            le nick à tester
     * @return si il s'agit d'un service Quakenet
     */
    public static boolean isQnetService(final String nick) {
        return nick.equalsIgnoreCase("L") || nick.equalsIgnoreCase("Q") || nick.equalsIgnoreCase("R")
                || nick.equalsIgnoreCase("O");
    }

    /**
     * L'auth Qnet.
     */
    private String qnetAuth;

    /**
     * @param nick1
     *            le nick
     */
    public QnetUser(final String nick1) {
        super(nick1);
    }

    /**
     * @return Returns the authName.
     */
    public final String getQnetAuth() {
        return this.qnetAuth;
    }

    /**
     * @return si je suis un service Qnet
     */
    public final boolean iAmQnetService() {
        return isQnetService(getNick());
    }

    /**
     * @param hostname1
     *            The hostname to set.
     */
    @Override
    public final void setHostname(final String hostname1) {
        super.setHostname(hostname1);
        if (StringUtils.isEmpty(this.qnetAuth)) {
            final int index = getHostname().indexOf(".users.quakenet.org");
            if (index > 1) {
                this.qnetAuth = getHostname().substring(0, index);
            }
        }
    }

    /**
     * @param string
     *            The authName to set.
     */
    public final void setQnetAuth(final String string) {
        this.qnetAuth = string;
    }
}
