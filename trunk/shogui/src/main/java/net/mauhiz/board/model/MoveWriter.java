package net.mauhiz.board.model;

import java.io.IOException;
import java.io.OutputStream;

import net.mauhiz.board.model.data.Game;

public interface MoveWriter {
	void writeAll(OutputStream out, Game game) throws IOException;

	void writeLast(OutputStream out, Game game) throws IOException;
}
