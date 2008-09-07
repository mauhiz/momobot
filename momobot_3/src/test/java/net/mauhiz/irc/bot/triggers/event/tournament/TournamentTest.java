package net.mauhiz.irc.bot.triggers.event.tournament;

import java.io.File;
import java.io.IOException;

import net.mauhiz.irc.base.data.defaut.DefaultChannel;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class TournamentTest {
    
    /**
     * Test method for {@link net.mauhiz.irc.bot.triggers.event.tournament.Tournament#generateTemplate()}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateTemplateFile() throws Exception {
        Tournament t = new Tournament(new DefaultChannel("#tsi.fr"), new String[]{"de_nuke", "de_tuscan"});
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
