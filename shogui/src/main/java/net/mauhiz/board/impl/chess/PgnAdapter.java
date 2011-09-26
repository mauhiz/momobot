package net.mauhiz.board.impl.chess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.board.impl.chess.data.ChessBoard.Status;
import net.mauhiz.board.impl.chess.data.ChessGame;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessRule;
import net.mauhiz.board.impl.common.data.NormalMoveImpl;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.MoveWriter;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

public enum PgnAdapter implements MoveReader, MoveWriter {
	INSTANCE;
	private static final Pattern CASTLE = Pattern.compile("O-O(-O)?([+#=])?");
	private static final Pattern SAN_MOVE = Pattern
			.compile("([a-hRNBQKP])?([a-h]?[1-8]?)?(x)?([a-h])([1-8])(=[RNBQ])?([+#=])?");

	private static Square findFrom(Game game, String fromPosition, Square to, PieceType moved, PlayerType player) {
		if (fromPosition != null && fromPosition.length() == 2) {
			return positionToSquare(fromPosition.codePointAt(0), fromPosition.codePointAt(2));
		}
		int position = fromPosition == null ? 0 : fromPosition.codePointAt(0);
		int col = 0;
		int row = 0;
		if (Character.isDigit(position)) {
			row = position - '1';
		} else {
			col = position - 'a';
		}
		for (Square square : game.getLastBoard().getSquares()) {
			if (col > 0 && square.getX() != col || row != 0 && square.getY() != row) {
				continue;
			}
			Piece cop = game.getLastBoard().getPieceAt(square);
			if (cop == null || cop.getPlayerType() != player || cop.getPieceType() != moved) {
				continue;
			}
			if (game.getRule().generateMove(square, to, game) != null) {
				return square;
			}
		}
		return null;
	}

	private static Square positionToSquare(int col, int row) {
		return SquareImpl.getInstance(col - 'a', row - '1');
	}

	private static void read(Game game, String pgnMove) {
		PlayerType player = game.getTurn();
		Move cm;
		Matcher castle = CASTLE.matcher(pgnMove);
		if (castle.matches()) {
			boolean great = castle.group(1) != null;
			cm = new Castle(player, great);
			//            String statusLine = castle.group(2);
			// status = readStatus(statusLine);
		} else {
			Matcher m = SAN_MOVE.matcher(pgnMove);
			if (m.matches()) {

				int i = 0;
				String movedPiece = m.group(++i);
				ChessPieceType moved = ChessPieceType.fromName(movedPiece == null ? "P" : movedPiece);
				String fromPosition = m.group(++i);
				++i; // capture
				int toX = m.group(++i).codePointAt(0);
				int toY = m.group(++i).codePointAt(0);
				Square to = positionToSquare(toX, toY);
				Square from = findFrom(game, fromPosition, to, moved, player);

				cm = new NormalMoveImpl(player, from, to);
				String promotionType = m.group(++i);
				if (promotionType != null) {
					ChessPieceType promotion = ChessPieceType.fromName(promotionType.substring(1));
					cm = new PromoteMove((NormalMove) cm, promotion);
				}
				//                String statusLine = m.group(++i);
				// status = readStatus(statusLine);
			} else {
				return;
			}
		}
		game.applyMove(cm);
	}

	static Status readStatus(String statusLine) {
		if (statusLine != null) {
			if (StringUtils.contains(statusLine, '#')) {
				return Status.MATE;
			} else if (StringUtils.contains(statusLine, '+')) {
				return Status.CHECK;
			} else if (StringUtils.contains(statusLine, '=')) {
				return Status.DRAW;
			}
		}

		return null;
	}

	/**
	 * @param square
	 * @return a pair of characters
	 */
	public static int[] squareToPosition(Square square) {
		return new int[] { square.getX() + 'a', square.getY() + '1' };
	}

	public Game readAll(InputStream data) throws IOException {
		Game game = new ChessGame(new ChessRule());
		String pgnData = IOUtils.toString(data);
		String[] pgnMoves = pgnData.split(" ");

		for (int i = 0; i < pgnMoves.length; i++) {
			if (i % 3 == 0) {
				// move #
				continue;
			}

			read(game, pgnMoves[i]);
		}

		return game;
	}

	public void readNext(InputStream data, Game game) throws IOException {
		String pgnMove = IOUtils.toString(data);
		read(game, pgnMove);
	}

	public void writeAll(OutputStream out, Game game) throws IOException {
		throw new NotImplementedException();
	}

	public void writeLast(OutputStream out, Game game) throws IOException {
		throw new NotImplementedException();
	}
}
