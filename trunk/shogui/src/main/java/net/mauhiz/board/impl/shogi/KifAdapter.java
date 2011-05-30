package net.mauhiz.board.impl.shogi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import net.mauhiz.board.impl.common.data.DropImpl;
import net.mauhiz.board.impl.common.data.InitMove;
import net.mauhiz.board.impl.common.data.NormalMoveImpl;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.impl.shogi.data.ShogiBoard;
import net.mauhiz.board.impl.shogi.data.ShogiGame;
import net.mauhiz.board.impl.shogi.data.ShogiPieceType;
import net.mauhiz.board.impl.shogi.data.ShogiRule;
import net.mauhiz.board.model.MoveReader;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Game;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.util.FileUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.IllegalClassException;

/**
 * @author mauhiz
 * Kif file format is a proprietary format for shogi logs.
 * - comment lines start with an asterisk (*)
 * - move lines start with a spaces ( )
 * - other lines can be ignored
 * 
 * A move line is either :
 * - a Normal move: 37 ２八飛(24) or  112 同　歩(63)
 * - a Drop : 38 ２四歩打
 * - a Promote move : 116 ５六角成(47)
 * - a Give-up : 144 投了
 * 
 * On a move line,
 * - the first token is the move number
 * - the next token is the destination square, in Japanese notation (arab numeral [file]+ japanese numeral [row])
 * - if the destination token is "同　", it means "same as previous"
 * - the next token (between brackets) is the originating square, in arab numerals ($file$row)
 */
public class KifAdapter implements MoveReader {
	private static final char DOU = '同';
	private static final String KANRYOU = "投了";
	private static final char NARI = '成';
	private static final char UCHI = '打';

	private static Square getSquareAA(char originFile, char originRow) {
		int originX = originFile - '0';
		int originY = originRow - '0';
		// in shogi, both are reversed
		return SquareImpl.getInstance(ShogiBoard.SIZE - originX, ShogiBoard.SIZE - originY);
	}

	private static Square getSquareAJ(char originFile, char originRow) {
		int originX = originFile - '０'; // full-width arab numeral
		// the Japanese numerals are not aligned in UTF-8
		int originY = JapaneseNumeral.valueOf(Character.toString(originRow)).getValue(); // kanji
		// in shogi, both are reversed
		return SquareImpl.getInstance(ShogiBoard.SIZE - originX, ShogiBoard.SIZE - originY);
	}

	public Game readAll(InputStream data) throws IOException {
		ShogiGame game = new ShogiGame(new ShogiRule());
		BufferedReader br = new BufferedReader(new InputStreamReader(data, Charset.forName("SHIFT-JIS")));

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			} else if (line.length() == 0) {
				continue;
			}

			if (line.charAt(0) == ' ') { // it's a move !
				readNext(IOUtils.toInputStream(line, FileUtil.UTF8.name()), game);
			}
		}
		return game;
	}

	public void readNext(InputStream data, Game game) throws IOException {
		String line = IOUtils.toString(data, FileUtil.UTF8.name());
		int i = 0;
		// chars 0..3 represent the move number.
		// char 4 is a space.
		i += 5;

		// destination file (arab numeral)
		char destFile = line.charAt(i++);
		Square dest;
		if (destFile == DOU) {
			if (game.getLastMove() instanceof InitMove) {
				// impossible!!
				throw new IllegalStateException("No previous move!");
			}
			Move previous = game.getLastMove();
			if (previous instanceof NormalMove) {
				dest = ((NormalMove) previous).getTo();
			} else if (previous instanceof Drop) {
				dest = ((Drop) previous).getTo();
			} else if (previous instanceof PromoteMove) {
				dest = ((PromoteMove) previous).getParentMove().getTo();
			} else {
				throw new IllegalClassException("Unsupported move class: " + previous.getClass());
			}
			i++; // skip space
		} else if (KANRYOU.charAt(0) == destFile) {
			return; // not a move, rather a status update
		} else {
			// the destination row (japanese numeral)
			char destRow = line.charAt(i++);
			try {
				dest = getSquareAJ(destFile, destRow);
			} catch (IllegalArgumentException iae) {
				throw new IllegalStateException("Invalid move: " + line);
			}
		}
		char piece = line.charAt(i++);
		// special move (or opening bracket if none)
		char special = line.charAt(i++);
		if (special == UCHI) {
			game.applyMove(new DropImpl(game.getTurn(), ShogiPieceType.fromKanji(piece), dest));
			return;
		} else if (special != '(') {
			i++;
		}
		// origin file (arab numeral)
		char originFile = line.charAt(i++);
		// origin row (arab numeral)
		char originRow = line.charAt(i++);
		Square origin = getSquareAA(originFile, originRow);
		NormalMove move = new NormalMoveImpl(game.getTurn(), origin, dest);
		if (special == NARI) {
			game.applyMove(new PromoteMove(move));
		} else {
			game.applyMove(move);
		}
	}
}
