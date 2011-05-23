package net.mauhiz.board.impl.shogi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.MoveWriter;
import net.mauhiz.board.model.data.Game;

import org.apache.commons.lang.NotImplementedException;

public class KifuAdapter implements MoveReader, MoveWriter {

	public Game readAll(InputStream data) throws IOException {
		throw new NotImplementedException("TODO");
	}

	public void readNext(InputStream data, Game game) throws IOException {
		throw new NotImplementedException("TODO");
	}

	public void writeAll(OutputStream out, Game game) throws IOException {
		throw new NotImplementedException("TODO");
	}

	public void writeLast(OutputStream out, Game game) throws IOException {
		throw new NotImplementedException("TODO");
	}

}
