package net.mauhiz.irc.bot.tournament;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import net.mauhiz.irc.base.data.Channel;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class TournamentTest {
    
    /**
     * Test method for {@link net.mauhiz.irc.bot.tournament.Tournament#power(int, int)}.
     * 
     */
    @Test
    public void powerTest() {
        Assert.assertEquals(32, Tournament.power(2, 5));
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.bot.tournament.Tournament#generateTemplate()}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateTemplateFile() throws Exception {
        Tournament t = new Tournament(new Channel("#tsi.fr"), new String[]{"de_nuke", "de_tuscan"});
        t.createTemplateFile();
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.bot.tournament.Tournament#generateTemplate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testUploadFile() throws IOException {
        File temp = new File("temp.html");
        Tournament.uploadFile(temp);
    }
    
}
