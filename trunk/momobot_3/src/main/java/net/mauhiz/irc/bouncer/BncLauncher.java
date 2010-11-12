package net.mauhiz.irc.bouncer;

/**
 * @author mauhiz
 */
public class BncLauncher {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        AccountStore dummy = new DummyAccountStore();
        dummy.reload();
        
        BncServerConnection bouncer = new BncServerConnection(dummy, 6667);
        bouncer.startAs("Bouncer Server");
    }
}
