package net.mauhiz.irc.bot.triggers.websearch;

import java.io.IOException;
import java.util.Arrays;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author mauhiz
 */
public class BabelfishTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * ma methode de post.
     */
    private static final HttpPost POST = new HttpPost("http://babelfish.altavista.com/tr");
    
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
        NameValuePair[] data = {new BasicNameValuePair("trtext", toTranslate),
                new BasicNameValuePair("lp", langue1 + '_' + langue2), new BasicNameValuePair("tt", "urltext"),
                new BasicNameValuePair("intl", "tt"), new BasicNameValuePair("doit", "done")};
        
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(data));
        POST.setEntity(entity);
        HttpResponse resp = new DefaultHttpClient().execute(POST);
        String page = IOUtils.toString(resp.getEntity().getContent());
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
