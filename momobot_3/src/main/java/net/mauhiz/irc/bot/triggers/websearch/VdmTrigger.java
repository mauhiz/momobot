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

/**
 * @author mauhiz
 */
public class VdmTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    
    /**
     * @return next bash quote
     */
    static String getNextVdm() {
        GetMethod get = new GetMethod("http://www.viedemerde.fr/aleatoire");
        try {
            new HttpClient().executeMethod(get);
            String page = get.getResponseBodyAsString();
            page = StringUtils.substringAfter(page, "<div class=\"post\"><p>");
            page = StringUtils.substringBefore(page, "</p>");
            page = StringUtils.replaceChars(page, "\r\n", "");
            return StringEscapeUtils.unescapeHtml(page);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    
    /**
     * @param trigger
     */
    public VdmTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String vdm = getNextVdm();
        while (vdm.length() > 200) {
            int spc = StringUtils.indexOf(vdm, ' ', 190);
            Privmsg reply = Privmsg.buildAnswer(im, vdm.substring(0, spc));
            control.sendMsg(reply);
            vdm = vdm.substring(spc + 1);
        }
        Privmsg reply = Privmsg.buildAnswer(im, vdm);
        control.sendMsg(reply);
    }
}
