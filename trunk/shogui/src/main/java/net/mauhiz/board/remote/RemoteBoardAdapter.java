package net.mauhiz.board.remote;

import java.io.IOException;

import net.mauhiz.board.model.BoardIO;
import net.mauhiz.board.model.GameController;
import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.PlayerType;

import org.apache.commons.io.IOUtils;

public class RemoteBoardAdapter implements GameController {

	private GameController localController;
	private MoveReader reader;

	public RemoteBoardAdapter(GameController localController, MoveReader reader) {
		this.localController = localController;
		this.reader = reader;
	}

	public BoardIO getBoardIO() {
		return localController.getBoardIO();
	}

	public Game getGame() {
		return localController.getGame();
	}

	public void readMove(String moveStr) throws IOException {
		reader.readNext(IOUtils.toInputStream(moveStr), getGame());
	}

	public PlayerType receiveMove(Move move) {
		BoardManager.getInstance().sendMove(this, move);
		return localController.receiveMove(move);
	}

}
