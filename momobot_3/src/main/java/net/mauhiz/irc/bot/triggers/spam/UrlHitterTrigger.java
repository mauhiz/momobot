package net.mauhiz.irc.bot.triggers.spam;

import java.io.IOException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author mauhiz
 */
public class UrlHitterTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public UrlHitterTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * TODO cron pour le url hitter ?
     * 
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String urlStr = getArgs(im.getMessage());
        HttpClient client = new HttpClient();
        HttpMethod get = new GetMethod(urlStr);
        String resp;
        try {
            client.executeMethod(get);
            resp = "done";
        } catch (IOException e) {
            resp = "erreur : " + e;
        }
        Privmsg reply = Privmsg.buildAnswer(im, resp);
        control.sendMsg(reply);
    }
}
