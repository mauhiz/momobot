package momobot.trigger;

import ircbot.ATrigger;
import ircbot.IrcUser;
import java.io.IOException;
import java.net.URLEncoder;
import momobot.MomoBot;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public class BabelfishTrigger extends ATrigger {
    /**
     * ma méthode de post.
     */
    private static final PostMethod POST = new PostMethod(
                                                 "http://babelfish.altavista.com/tr");

    /**
     * @param langue1
     *            la langue de départ
     * @param langue2
     *            la langue de destination
     * @param toTranslate
     *            le message à traduire
     * @return le message traduit
     * @throws IOException
     *             si le site est en mousse
     */
    public static String result(final String langue1, final String langue2,
            final String toTranslate) throws IOException {
        final NameValuePair[] data = {
                new NameValuePair("trtext", URLEncoder.encode(toTranslate,
                        "utf-8")),
                new NameValuePair("lp", URLEncoder.encode(langue1 + '_'
                        + langue2, "utf-8")),
                new NameValuePair("tt", "urltext"),
                new NameValuePair("intl", "tt"),
                new NameValuePair("doit", "done")
        };
        POST.setRequestBody(data);
        new HttpClient().executeMethod(POST);
        String page = POST.getResponseBodyAsString();
        final String bound1 = "<td bgcolor=white class=s><div style=padding:10px;>";
        final int len = bound1.length();
        int i = page.indexOf(bound1) + len;
        if (i <= len) {
            throw new IOException("Échec ://");
        }
        page = page.substring(i);
        final String bound2 = "</div></td>";
        i = page.indexOf(bound2);
        return page.substring(0, i);
    }

    /**
     * @param trigger
     *            le trigger
     */
    public BabelfishTrigger(final String trigger) {
        super(trigger);
        setPublic(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     * @param user
     *            le user
     * @param message
     *            le message
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        throw new UnsupportedOperationException();
    }
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(BabelfishTrigger.class);

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     * @param channel
     *            le channel
     * @param user
     *            le user
     * @param message
     *            le message
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        try {
            String msg = getArgs(message);
            final String lang1 = StringUtils.substringBefore(msg, " ");
            msg = StringUtils.substringAfter(msg, " ");
            final String lang2 = StringUtils.substringBefore(msg, " ");
            msg = StringUtils.substringAfter(msg, " ");
            MomoBot.getInstance().sendNotice(user,
                    result(lang1, lang2, message));
        } catch (final Exception e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(e, e);
            }
            MomoBot.getInstance().sendNotice(
                    user,
                    "syntaxe : $" + getTriggerText()
                            + " lang1[fr/en/..] lang2[fr/en/...] texte");
        }
    }
}
