package net.mauhiz.irc.bot.event;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Abby
 * 
 */

public class SeekWar2Test {
    /**
     * 
     */
    @Test
    public void isMatchAgreeMessageTest() {
        Assert.assertTrue(SeekWar2.isMatchAgreeMessage("k"));
        
        Assert.assertTrue(SeekWar2.isMatchAgreeMessage("go"));
        
        Assert.assertFalse(SeekWar2.isMatchAgreeMessage("tg"));
    }
    
    @Test
    public void isMatchIpTest() {
        
        // Exemple classique
        Assert.assertTrue(SeekWar2.isMatchIp("192.168.0.15:27015"));
        
        // Attension ya un pi�ge l�...
        Assert.assertFalse(SeekWar2.isMatchIp("300.168.0.15:27015"));
        
        // Avec 5 chiffres aux lieux de 4
        // FIXME ca va matcher sur "168.0.15.1:27015" vu la regexp...
        Assert.assertFalse(SeekWar2.isMatchIp("192.168.0.15.1:27015"));
        
        // Avec 3 chiffres au lieux de 3
        Assert.assertFalse(SeekWar2.isMatchIp("192.1680.15:27015"));
        
        // Avec un caract�re alphab�tique
        Assert.assertFalse(SeekWar2.isMatchIp("192.168.a.15:27015"));
        
    }
    
    @Test
    public void isMatchUrlTest() {
        Assert.assertTrue(SeekWar2.isMatchUrl("www.mauhiz.net"));
        
        Assert.assertFalse(SeekWar2.isMatchUrl("wrong"));
        
    }
    
}
