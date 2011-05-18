package net.mauhiz.board.impl.shogi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

public class KifAdapterTest {

	@Test
	public void testKif() throws IOException {
		File resFile = new File("src/test/resources/20070707asahi_10.kif");
		KifAdapter kifa = new KifAdapter();
		FileInputStream fis = new FileInputStream(resFile);
		try {
			System.out.println(kifa.readAll(fis));
		} finally {
			fis.close();
		}
	}
}
