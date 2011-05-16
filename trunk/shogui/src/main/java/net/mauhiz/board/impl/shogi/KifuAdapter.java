package net.mauhiz.board.impl.shogi;

import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.MoveWriter;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

import org.apache.commons.lang.NotImplementedException;

public class KifuAdapter implements MoveReader, MoveWriter {

	@Override
	public String write(Game game, Move move) {
		throw new NotImplementedException("TODO");
	}

	@Override
	public Move read(Game game, String moveStr) {
		throw new NotImplementedException("TODO");
	}
	
}
