package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.data.defaut.DefaultUser;

/**
 * Simule un utilisateur
 * 
 * @author mauhiz
 */
public class FakeUser extends DefaultUser {
    /**
     * @param name
     */
    public FakeUser(String name) {
        super(name);
    }
}
