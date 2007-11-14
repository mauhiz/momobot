package momobot;

import java.sql.SQLException;

import momobot.event.AddTrigger;
import momobot.event.GatherTrigger;
import momobot.event.MassHlTrigger;
import momobot.event.PickupTrigger;
import momobot.event.RmvTrigger;
import momobot.event.RollTrigger;
import momobot.event.StatusTrigger;
import momobot.event.StopTrigger;
import momobot.memo.DbMemoUtils;
import momobot.memo.ListMemosTrigger;
import momobot.memo.MemoTrigger;
import momobot.trigger.ActTrigger;
import momobot.trigger.HelpTrigger;
import momobot.trigger.LengthTrigger;
import momobot.trigger.SayTrigger;
import momobot.trigger.admin.CountUsersTrigger;
import momobot.trigger.admin.JoinTrigger;
import momobot.trigger.admin.PartTrigger;
import momobot.trigger.admin.QuitTrigger;
import momobot.trigger.fun.KennyTrigger;
import momobot.trigger.fun.MargueriteTrigger;
import momobot.trigger.fun.Q3NickTrigger;
import momobot.trigger.math.FractionContinueTrigger;
import momobot.trigger.math.Md5Trigger;
import momobot.trigger.math.Sha256Trigger;
import momobot.trigger.math.Sha512Trigger;
import momobot.websearch.GoogleTrigger;
import momobot.whois.WhoisTrigger;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Launcher {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Launcher.class);

    /**
     * Lance le bot.
     * 
     * @param args
     *            les arguments
     * @throws SQLException
     *             exception SQL
     */
    public static void main(final String... args) throws SQLException {
        /* init */
        if (ArrayUtils.isEmpty(args)) {
            LOG.fatal("Syntaxe : " + Launcher.class.getName() + " profil");
            return;
        }
        DbUtils.loadDriver("com.mysql.jdbc.Driver");
        SqlUtils.loadPlayerDB();
        DbMemoUtils.loadMemoDB();
        /* admin */
        // new ActivateTrigger("$activate");
        new JoinTrigger("$join");
        new QuitTrigger("$taggle");
        new PartTrigger("$part");
        /* base */
        new HelpTrigger("$help");
        new ActTrigger("$act");
        new SayTrigger("$say");
        new WhoisTrigger("$whois");
        new QuitTrigger("$quit");
        /* fun */
        new KennyTrigger("$kenny");
        new GoogleTrigger("$google");
        new LengthTrigger("$len");
        new FractionContinueTrigger("$fracont");
        new Md5Trigger("$md5");
        new Sha256Trigger("$sha256");
        new Sha512Trigger("$sha512");
        new CountUsersTrigger("$count");
        new MargueriteTrigger("$love");
        new Q3NickTrigger("$q3");
        /* memo */
        new MemoTrigger("$memo");
        new ListMemosTrigger("$listmemos");
        /* gather */
        new AddTrigger("$add");
        new GatherTrigger("$start");
        new PickupTrigger("$pickup");
        new MassHlTrigger("$fessee");
        new StatusTrigger("$status");
        new RollTrigger("$roll");
        new RmvTrigger("$rmv");
        new StopTrigger("$reset");
        /* FIN */
        SqlUtils.loadMomoBot(args[0]);
        // Server s = new Server(new InetSocketAddress("203.26.94.233", 27015));
        // s.getDetails();
    }

    /**
     * Constructeur caché.
     */
    protected Launcher() {
        throw new UnsupportedOperationException();
    }
}
