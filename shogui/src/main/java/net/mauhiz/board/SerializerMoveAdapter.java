package net.mauhiz.board;

import java.io.Serializable;

import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.MoveWriter;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.SerializationUtils;

/**
 * Default serialization for moves
 * @author mauhiz
 */
public class SerializerMoveAdapter implements MoveReader, MoveWriter {

	@Override
	public String write(Game game, Move move) {
		if (move instanceof Serializable) {
			return new String(SerializationUtils.serialize((Serializable) move), FileUtil.UTF8);
		}
		return null;
	}

	@Override
	public Move read(Game game, String moveStr) {
		 return (Move) SerializationUtils.deserialize(moveStr.getBytes(FileUtil.UTF8));
	}
}
