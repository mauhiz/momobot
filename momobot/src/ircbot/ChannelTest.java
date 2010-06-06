package ircbot;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChannelTest {

	@Test
	public void testIsChannelName() {
		assertTrue(Channel.isChannelName("#tsi.fr"));
		
		assertFalse(Channel.isChannelName("tsi.fr"));
		
		assertFalse(Channel.isChannelName(" tsi.fr"));
	}

}
