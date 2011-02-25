package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.util.NetUtils;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class HltvTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    private static final String CONNECT = "connect";
    private static final String DISCONNECT = "disconnect";
    private static RconServer hltv;
    private static boolean recording;
    private static final String SELECT = "select";
    private static final String START = "start";
    private static final String STATUS = "status";
    private static final String STOP = "stop";
    
    /**
     * @param trigger
     *            le trigger
     */
    public HltvTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String reply;
        try {
            reply = doTrigger(getArgs(im.getMessage()));
            
        } catch (SocketTimeoutException ste) {
            reply = "HLTV server offline: " + hltv.getIp();
            
        } catch (IOException e) {
            LOG.warn(e, e);
            reply = "Error while connecting to HLTV: " + e;
        }
        
        Privmsg msg = Privmsg.buildAnswer(im, reply);
        control.sendMsg(msg);
    }
    
    private String doTrigger(String args0) throws IOException {
        String cmd = StringUtils.substringBefore(args0, " ");
        
        if (SELECT.equalsIgnoreCase(cmd)) {
            String args = StringUtils.substringAfter(args0, " ");
            String ip = StringUtils.substringBefore(args, " ");
            String rcon = StringUtils.substringAfter(args, " ");
            
            hltv = new RconServer(NetUtils.makeISA(ip), rcon);
            hltv.getClient().changeRcon(rcon);
            return "HLTV selected: " + ip;
            
        } else if (CONNECT.equalsIgnoreCase(cmd)) {
            
            String args = StringUtils.substringAfter(args0, " ");
            String ip = StringUtils.substringBefore(args, " ");
            InetSocketAddress ipay = NetUtils.makeISA(ip);
            if (ipay == null) {
                return "Invalid server IP: " + ip;
            } else if (hltv == null) {
                return "Please select a HLTV before";
            } else {
                String pass = StringUtils.substringAfter(args, " ");
                hltv.getClient().rconCmd("serverpassword " + pass);
                hltv.getClient().rconCmd("connect " + ip);
                return "Connecting...";
            }
        } else if (DISCONNECT.equalsIgnoreCase(cmd)) {
            recording = false;
            hltv.getClient().rconCmd("disconnect");
            return "Disconnecting";
            
        } else if (START.equalsIgnoreCase(cmd)) {
            hltv.getClient().rconCmd("record ");
            if (recording) {
                return "A demo is already being recorded";
            }
            recording = true;
            
            String args = StringUtils.substringAfter(args0, " ");
            return "Starting demo: " + args;
        } else if (STOP.equalsIgnoreCase(cmd)) {
            if (recording) {
                recording = false;
                hltv.getClient().rconCmd("stoprecording");
                return "Stopping demo";
            }
            return "No demo was being recorded";
        } else if (STATUS.equalsIgnoreCase(cmd)) {
            if (hltv == null) {
                return "No HLTV selected";
            }
            if (recording) {
                return "HLTV recording";
            }
            return "HLTV stopped";
        } else {
            return getTriggerHelp();
        }
    }
    
    @Override
    public String getTriggerHelp() {
        return "Syntaxe: " + getTriggerText()
                + " [select ip:port rcon | connect ip:port pw | disconnect | start demoname | stop | status]";
    }
}
