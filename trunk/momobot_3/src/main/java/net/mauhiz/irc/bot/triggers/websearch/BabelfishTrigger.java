package net.mauhiz.irc.bot.triggers.websearch;

import java.io.IOException;
import java.net.URLEncoder;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.util.FileUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class BabelfishTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * encodage.
     */
    private static final String ENCODE = FileUtil.UTF8.name();
    /**
     * ma methode de post.
     */
    private static final PostMethod POST = new PostMethod("http://babelfish.altavista.com/tr");
    
    /**
     * @param langue1
     *            la langue de depart
     * @param langue2
     *            la langue de destination
     * @param toTranslate
     *            le message a traduire
     * @return le message traduit
     * @throws IOException
     *             si le site est en mousse
     */
    public static String result(String langue1, String langue2, String toTranslate) throws IOException {
        NameValuePair[] data = {new NameValuePair("trtext", URLEncoder.encode(toTranslate, ENCODE)),
                new NameValuePair("lp", URLEncoder.encode(langue1 + '_' + langue2, ENCODE)),
                new NameValuePair("tt", "urltext"), new NameValuePair("intl", "tt"), new NameValuePair("doit", "done")};
        POST.setRequestBody(data);
        new HttpClient().executeMethod(POST);
        String page = POST.getResponseBodyAsString();
        String bound1 = "<td bgcolor=white class=s><div style=padding:10px;>";
        int len = bound1.length();
        int index = page.indexOf(bound1) + len;
        if (index <= len) {
            throw new IOException("echec ://");
        }
        page = page.substring(index);
        String bound2 = "</div></td>";
        index = page.indexOf(bound2);
        return page.substring(0, index);
    }
    
    /**
     * @param trigger
     *            le trigger
     */
    public BabelfishTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        
        String msg = getArgs(cme.getMessage());
        String lang1 = StringUtils.substringBefore(msg, " ");
        msg = StringUtils.substringAfter(msg, " ");
        String lang2 = StringUtils.substringBefore(msg, " ");
        msg = StringUtils.substringAfter(msg, " ");
        Notice notice;
        try {
            notice = Notice.buildAnswer(cme, result(lang1, lang2, msg));
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
            notice = Notice.buildPrivateAnswer(cme, "syntaxe : " + this + " lang1[fr/en/..] lang2[fr/en/...] texte");
        }
        control.sendMsg(notice);
    }
}
