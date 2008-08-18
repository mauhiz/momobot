package net.mauhiz.irc.bot.triggers.websearch;

import java.io.IOException;
import java.net.URLEncoder;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class BabelfishTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * encodage.
     */
    private static final String ENCODE = "utf-8";
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(BabelfishTrigger.class);
    /**
     * ma méthode de post.
     */
    private static final PostMethod POST = new PostMethod("http://babelfish.altavista.com/tr");
    
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
    public static String result(final String langue1, final String langue2, final String toTranslate)
            throws IOException {
        final NameValuePair[] data = {new NameValuePair("trtext", URLEncoder.encode(toTranslate, ENCODE)),
                new NameValuePair("lp", URLEncoder.encode(langue1 + '_' + langue2, ENCODE)),
                new NameValuePair("tt", "urltext"), new NameValuePair("intl", "tt"), new NameValuePair("doit", "done"),};
        POST.setRequestBody(data);
        new HttpClient().executeMethod(POST);
        String page = POST.getResponseBodyAsString();
        final String bound1 = "<td bgcolor=white class=s><div style=padding:10px;>";
        final int len = bound1.length();
        int index = page.indexOf(bound1) + len;
        if (index <= len) {
            throw new IOException("Échec ://");
        }
        page = page.substring(index);
        final String bound2 = "</div></td>";
        index = page.indexOf(bound2);
        return page.substring(0, index);
    }
    
    /**
     * @param trigger
     *            le trigger
     */
    public BabelfishTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        
        String msg = getArgs(cme.getMessage());
        final String lang1 = StringUtils.substringBefore(msg, " ");
        msg = StringUtils.substringAfter(msg, " ");
        final String lang2 = StringUtils.substringBefore(msg, " ");
        msg = StringUtils.substringAfter(msg, " ");
        Notice notice;
        try {
            notice = Notice.buildAnswer(cme, result(lang1, lang2, msg));
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
            notice = Notice.buildPrivateAnswer(cme, "syntaxe : " + this + " lang1[fr/en/..] lang2[fr/en/...] texte");
        }
        control.sendMsg(notice);
    }
}
