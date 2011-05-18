package net.mauhiz.board.impl.shogi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.junit.Test;

public class KifAdapterTest {

	@Test
	public void testImportKif() throws IOException, URISyntaxException {
		KifAdapter kifa = new KifAdapter();
		String live = "http://live.shogi.or.jp/asahi/archives/1/kifu/20070707asahi_10.html";
		InputStream is = KifImporter.importKif(live);
		try {
			System.out.println(kifa.readAll(is));
		} finally {
			is.close();
		}
	}

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
