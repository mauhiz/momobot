package net.mauhiz.board.model;

import java.io.IOException;
import java.io.InputStream;

import net.mauhiz.board.model.data.Game;

public interface MoveReader {
	Game readAll(InputStream data) throws IOException;

	void readNext(InputStream data, Game game) throws IOException;
}
