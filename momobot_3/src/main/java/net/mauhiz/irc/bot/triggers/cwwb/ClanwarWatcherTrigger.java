package net.mauhiz.irc.bot.triggers.cwwb;
import java.io.IOException;

import net.mauhiz.irc.HibernateUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.bot.triggers.IPartTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.IQuitTrigger;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * @author abby
 */
public class ClanwarWatcherTrigger implements IPrivmsgTrigger, IPartTrigger, IQuitTrigger {
    public static final String[] IGNORED_NICKS = {"[CW|FR]", "Q", "S"};
    
    /**
     * @throws IOException
     */
    public ClanwarWatcherTrigger() throws IOException {
        super();
        
        // On vide les tables direct en arrivant
        // TODO : permettre un paramétrage de cette fonction ?
        SQLQuery trunc = HibernateUtils.currentSession().createSQLQuery("truncate table WAR");
        trunc.executeUpdate();
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPartTrigger#doTrigger(net.mauhiz.irc.base.msg.Part,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    public void doTrigger(final Part im, final IIrcControl control) {
        removeSeekFromUser(im.getFrom());
        
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        String message = im.getMessage();
        String sender = im.getFrom();
        // On ignore les messages trop courts pour être utile
        if (message.length() <= 3) {
            return;
        }
        // On ignore les messages provenant de certains utilisateurs.
        for (String element : IGNORED_NICKS) {
            if (sender == element) {
                return;
            }
        }
        
        // Commande de parsage des wars
        processMessage(sender, message);
        
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IQuitTrigger#doTrigger(net.mauhiz.irc.base.msg.Quit,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    public void doTrigger(final Quit im, final IIrcControl control) {
        removeSeekFromUser(im.getFrom());
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.ITextTrigger#getTriggerHelp()
     */
    public String getTriggerHelp() {
        return null;
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.ITextTrigger#isActivatedBy(java.lang.String)
     */
    public boolean isActivatedBy(final String text) {
        return true;
    }
    
    /**
     * @param sender
     * @param pMessage
     */
    public void processMessage(final String sender, final String pMessage) {
        String message = pMessage.trim();
        // String lowMsg = message.toLowerCase();
        
        // TODO : est-ce que l'objet war est bien supprimé a chaque fois ?
        War war = new War(sender, message);
        
        // Si le parser n'a pas réussi a trouver une war on arrete
        
        HibernateUtils.currentSession().getTransaction().begin();
        if (war.isNull()) {
            /*
             * TODO : Rajouter une option debug ou pas : pour faire une version du bot plus légère.
             */
            Untreated utt = new Untreated();
            utt.setMessage(message);
            utt.setUser(sender);
            HibernateUtils.currentSession().save(utt);
        } else {
            /*
             * Insertion du message dans la bdd. On insert le nouveau seek, en remplacant l'ancien si un seek du meme
             * joueur existe déjà
             */
            Query qry = HibernateUtils.currentSession().createQuery("from War where user = :user");
            qry.setString("user", sender);
            War existing = (War) qry.uniqueResult();
            if (existing != null) {
                war.setId(existing.getId());
                HibernateUtils.currentSession().delete(existing);
            }
            HibernateUtils.currentSession().save(war);
        }
        HibernateUtils.currentSession().getTransaction().commit();
    }
    
    /**
     * Enleve les seeks des user ayant quit clanwar de la bdd
     * 
     * @param user
     */
    public void removeSeekFromUser(final String user) {
        SQLQuery qry = HibernateUtils.currentSession().createSQLQuery("delete from War where user = :user");
        qry.setString("user", user);
        qry.executeUpdate();
    }
}
