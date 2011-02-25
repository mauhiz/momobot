package net.mauhiz.irc.bot.triggers.dispo;

import java.util.Calendar;
import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.data.WhoisRequest;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.dispo.Dispo.Present;
import net.mauhiz.util.DateUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

/**
 * @author mauhiz
 */
public class DispoTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    private static final String[] DISPOS = {"oui", "non", "?"};
    private static final String[] HEURES = {"21h", "22h30"};
    /**
     * @param trigger
     *            le trigger
     */
    public DispoTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        IrcUser user = cme.getServer().findUser(new Mask(cme.getFrom()), false);
        if (!(user instanceof QnetUser)) {
            LOG.error("user non Qnet: " + user);
            return;
        }
        QnetUser quser = (QnetUser) user;
        whoisUser(quser, cme, control);
        
        StrTokenizer tokenizer = new StrTokenizer(getArgs(cme.getMessage()));
        
        String[] args = tokenizer.getTokenArray();
        Calendar date = null;
        if (!ArrayUtils.isEmpty(args)) {
            date = DateUtil.getDateFromJour(args[0], Locale.FRANCE); /* tokens finis */
        }
        
        Notice notice;
        if (date == null) {
            StringBuilder msg = new StringBuilder();
            msg.append("syntaxe : ").append(this).append(" jour[");
            msg.append(StringUtils.join(DateUtil.getWeekDays(Locale.FRANCE), '/'));
            msg.append("] ");
            for (String heure : HEURES) {
                msg.append(heure).append("[").append(StringUtils.join(DISPOS, '/')).append("] ");
            }
            notice = Notice.buildPrivateAnswer(cme, msg.toString());
        } else {
            Dispo dispo = new Dispo();
            dispo.setChannel(cme.getTo());
            Present[] heures = new Present[HEURES.length];
            for (int i = 0; i < HEURES.length && i < args.length - 1; i++) {
                String nextArg = args[i + 1];
                if (nextArg.equalsIgnoreCase("oui")) {
                    heures[i] = Present.LA;
                } else if (nextArg.equalsIgnoreCase("non")) {
                    heures[i] = Present.PAS_LA;
                }
            }
            dispo.setPresent1(heures[0]);
            dispo.setPresent2(heures[1]);
            dispo.setQauth(quser.getAuth());
            dispo.setServerAlias(cme.getServer().getAlias());
            dispo.setQuand(new java.sql.Date(date.getTimeInMillis()));
            DispoDb.updateDispo(dispo);
            notice = Notice.buildPrivateAnswer(cme, "dispo enregistree pour le " + DateUtil.DATE_FORMAT.format(date));
        }
        control.sendMsg(notice);
    }
    
    private void whoisUser(QnetUser quser, IIrcMessage cme, IIrcControl control) {
        if (StringUtils.isEmpty(quser.getAuth())) {
            WhoisRequest whois = new WhoisRequest(quser.getNick(), cme.getServer(), control);
            whois.startAs("Whois Request");
            
            /* on attend le whois */
            while (whois.isRunning()) {
                Thread.yield();
            }
            
            if (StringUtils.isEmpty(quser.getAuth())) {
                Notice notice = Notice.buildPrivateAnswer(cme,
                        "il faut etre auth sur Qnet pour utiliser cette fonction");
                control.sendMsg(notice);
                return;
            }
        }
    }
}
