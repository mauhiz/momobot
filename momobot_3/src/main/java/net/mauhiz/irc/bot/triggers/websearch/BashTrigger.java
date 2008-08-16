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
    static final String[] getNextFrQuote() {
        GetMethod get = new GetMethod("http://www.bashfr.org/?sort=random2");
        try {
            new HttpClient().executeMethod(get);
            String page = get.getResponseBodyAsString();
            page = StringUtils.substringAfter(page, " class=\"com\"></span><br />");
            page = StringUtils.substringBefore(page, "</div>");
            page = StringUtils.replaceChars(page, "\r\n", "");
            String[] lignes = new StrTokenizer(page, "<br />").getTokenArray();
            for (int i = 0; i < lignes.length; ++i) {
                lignes[i] = StringEscapeUtils.unescapeHtml(lignes[i]);
            }
            return lignes;
        } catch (IOException e) {
            return new String[]{e.getMessage()};
        }
    }
    
    /**
     * TODO bash.org est offline en ce moment...
     * 
     * @return next bash quote
     */
    static final String[] getNextQuote() {
        GetMethod get = new GetMethod("http://www.bash.org/?random");
        try {
            new HttpClient().executeMethod(get);
            String page = get.getResponseBodyAsString();
            String[] lignes = new StrTokenizer(page, "<br />").getTokenArray();
            return lignes;
        } catch (IOException e) {
            return new String[]{e.getMessage()};
        }
    }
    
    /**
     * @param trigger
     */
    public BashTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        // String args = getArgs(im.getMessage());
        String[] quote;
        // if ("fr".equals(args)) {
        quote = getNextFrQuote();
        // } else {
        // quote = getNextQuote();
        // }
        for (String quoteLine : quote) {
            Privmsg reply = Privmsg.buildAnswer(im, quoteLine);
            control.sendMsg(reply);
        }
    }
}
