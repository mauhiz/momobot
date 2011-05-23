package net.mauhiz.board;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;

import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.MoveWriter;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.SerializationUtils;

/**
 * Default serialization for moves
 * @author mauhiz
 */
public class SerializerMoveAdapter implements MoveReader, MoveWriter {

	public Game readAll(InputStream data) throws IOException {
		ObjectInputStream in = new ObjectInputStream(data);
		try {
			return (Game) in.readObject();
		} catch (ClassNotFoundException ex) {
			throw new SerializationException(ex);
		} catch (ObjectStreamException e) {
			return null; // EOF
		}
	}

	protected Move readMove(InputStream data) throws IOException {
		ObjectInputStream in = new ObjectInputStream(data);
		try {
			return (Move) in.readObject();
		} catch (ClassNotFoundException ex) {
			throw new SerializationException(ex);
		} catch (ObjectStreamException e) {
			return null; // EOF
		}
	}

	public void readNext(InputStream data, Game game) throws IOException {
		game.applyMove(readMove(data));
	}

	public void writeAll(OutputStream out, Game game) throws IOException {
		for (Move move : game.getMoves()) {
			writeMove(out, move);
		}
	}

	public void writeLast(OutputStream out, Game game) throws IOException {
		writeMove(out, game.getLastMove());
	}

	protected void writeMove(OutputStream out, Move move) throws IOException {
		if (move instanceof Serializable) {
			out.write(SerializationUtils.serialize((Serializable) move));
		} else {
			throw new IllegalClassException(Serializable.class, move);
		}
	}
}
