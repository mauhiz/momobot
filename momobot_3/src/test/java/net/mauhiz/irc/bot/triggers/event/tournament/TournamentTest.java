package net.mauhiz.irc.bot.triggers.event.tournament;

import java.io.File;
import java.io.IOException;

import net.mauhiz.irc.AbstractServerTest;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class TournamentTest extends AbstractServerTest {
    
    /**
     * Test method for {@link net.mauhiz.irc.bot.triggers.event.tournament.Tournament#generateTemplate()}.
     */
    @Test
    public void testCreateTemplateFile() throws IOException {
        Tournament t = new Tournament(QNET.newChannel("#tsi.fr"), new String[]{"de_nuke", "de_tuscan"});
        t.createTemplateFile();
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.bot.triggers.event.tournament.Tournament#generateTemplate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testUploadFile() throws IOException {
        File temp = new File("temp.html");
        Tournament.uploadFile(temp);
    }
    
}
