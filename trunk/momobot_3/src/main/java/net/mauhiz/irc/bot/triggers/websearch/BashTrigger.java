package net.mauhiz.irc.bot.triggers.websearch;

import java.io.IOException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

/**
 * @author mauhiz
 */
public class BashTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @return next bashfr quote
     */
    static String[] getNextFrQuote() {
        GetMethod get = new GetMethod("http://www.bashfr.org/?sort=random2");
        try {
            new HttpClient().executeMethod(get);
            String page = get.getResponseBodyAsString();
            page = StringUtils.substringAfter(page, " class=\"com\"></span><br />");
            page = StringUtils.substringBefore(page, "</div>");
            page = StringUtils.replaceChars(page, "\r\n", "");
            String[] lignes = new StrTokenizer(page, "<br />").getTokenArray();
            for (int i = 0; i < lignes.length; i++) {
                lignes[i] = StringEscapeUtils.unescapeHtml(lignes[i]);
            }
            return lignes;
        } catch (IOException e) {
            return new String[]{e.getMessage()};
        }
    }
    
    /**
     * @return next bash quote
     */
    static String[] getNextQuote() {
        GetMethod get = new GetMethod("http://www.bash.org/?random");
        try {
            new HttpClient().executeMethod(get);
            String page = get.getResponseBodyAsString();
            page = StringUtils.substringAfter(page, "<p class=\"qt\">");
            page = StringUtils.substringBefore(page, "</p>");
            page = StringUtils.replaceChars(page, "\r\n", "");
            String[] lignes = new StrTokenizer(page, "<br />").getTokenArray();
            for (int i = 0; i < lignes.length; i++) {
                lignes[i] = StringEscapeUtils.unescapeHtml(lignes[i]);
            }
            return lignes;
        } catch (IOException e) {
            return new String[]{e.getMessage()};
        }
    }
    
    /**
     * @param trigger
     */
    public BashTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String args = getArgs(im.getMessage());
        String[] quote;
        if ("fr".equals(args)) {
            quote = getNextFrQuote();
        } else {
            quote = getNextQuote();
        }
        for (String quoteLine : quote) {
            Privmsg reply = Privmsg.buildAnswer(im, quoteLine);
            control.sendMsg(reply);
        }
    }
}
