package net.mauhiz.contest.facebook.challenge;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mauhiz.contest.MultipleSolver;

public class Chess2 extends MultipleSolver {
	static boolean canRookReach(Pieces[][] board, Point currentPos, Point targetPos) {
		if (targetPos.x == currentPos.x) {
			int minY = Math.min(currentPos.y, targetPos.y);
			int maxY = Math.max(currentPos.y, targetPos.y);
			for (int f = minY + 1; f < maxY; f++) {
				if (board[targetPos.x][f] != null) {
					return false;
				}
			}
			return true;
		} else if (targetPos.y == currentPos.y) {
			int minX = Math.min(currentPos.x, targetPos.x);
			int maxX = Math.max(currentPos.x, targetPos.x);
			for (int r = minX + 1; r < maxX; r++) {
				if (board[r][targetPos.y] != null) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	static boolean canBishopReach(Pieces[][] board, Point currentPos, Point targetPos) {
		// moves SW/NE
		if (targetPos.x - currentPos.x == targetPos.y - currentPos.y) {
			int minX = Math.min(currentPos.x, targetPos.x);
			int maxX = Math.max(currentPos.x, targetPos.x);
			int minY = Math.min(currentPos.y, targetPos.y);
			for (int r = minX + 1; r < maxX; r++) {
				if (board[r][minY + r - minX] != null) {
					return false;
				}
			}
			return true;
			// NW/SE
		} else if (targetPos.x - currentPos.x == currentPos.y - targetPos.y) {
			int minX = Math.min(currentPos.x, targetPos.x);
			int maxX = Math.max(currentPos.x, targetPos.x);
			int maxY = Math.max(currentPos.y, targetPos.y);
			for (int r = minX + 1; r < maxX; r++) {
				if (board[r][maxY - r + minX] != null) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	static boolean canKnightReach(Pieces[][] board, Point currentPos, Point targetPos) {
		if (Math.abs(targetPos.x - currentPos.x) == 1) {
			return Math.abs(targetPos.y - currentPos.y) == 2;
		} else if (Math.abs(targetPos.x - currentPos.x) == 2) {
			return Math.abs(targetPos.y - currentPos.y) == 1;
		} else {
			return false;
		}
	}

	static enum Pieces {
		KING('K') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return targetPos.x - currentPos.x <= 1 && targetPos.y - currentPos.y <= 1;
			}
		},
		QUEEN('Q') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return canRookReach(board, currentPos, targetPos) || canBishopReach(board, currentPos, targetPos);
			}
		},
		ROOK('R') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return canRookReach(board, currentPos, targetPos);
			}
		},
		BISHOP('B') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return canBishopReach(board, currentPos, targetPos);
			}
		},
		KRAKEN('E') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return true;
			}
		},
		KNIGHT('N') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return canKnightReach(board, currentPos, targetPos);
			}
		},
		NIGHTRIDER('S') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				int xMoves = targetPos.x - currentPos.x;
				int yMoves = targetPos.y - currentPos.y;

				if (Math.abs(xMoves) % 2 == 0) {
					if (xMoves / 2 == yMoves) {
						boolean right = xMoves > 0;
						int minY = Math.min(currentPos.y, targetPos.y);
						int maxY = Math.max(currentPos.y, targetPos.y);
						int startX = right ? Math.min(currentPos.x, targetPos.x) : Math.max(currentPos.x, targetPos.x);
						for (int r = minY + 1; r < maxY; r++) {
							if (board[right ? startX + 2 * (r - minY) : startX - 2 * (r - minY)][r] != null) {
								return false;
							}
						}
					}
				} else if (Math.abs(yMoves) % 2 == 0) {
					if (yMoves / 2 == xMoves) {
						boolean up = xMoves > 0;
						int minX = Math.min(currentPos.x, targetPos.x);
						int maxX = Math.max(currentPos.x, targetPos.x);
						int startY = up ? Math.min(currentPos.y, targetPos.y) : Math.max(currentPos.y, targetPos.y);
						for (int f = minX + 1; f < maxX; f++) {
							if (board[f][up ? startY + 2 * (f - minX) : startY - 2 * (f - minX)] != null) {
								return false;
							}
						}
					}
				}
				return false;

			}
		},
		ARCHBISHOP('A') {
			@Override
			public boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos) {
				return canBishopReach(board, currentPos, targetPos) || canKnightReach(board, currentPos, targetPos);
			}
		};
		private char id;

		private Pieces(char id) {
			this.id = id;
		}

		public abstract boolean isReachable(Pieces[][] board, Point currentPos, Point targetPos);

		static Pieces fromId(char c) {
			for (Pieces p : values()) {
				if (p.id == c) {
					return p;
				}
			}

			return null;
		}
	}

	static class PieceOnBoard {
		Pieces p;
		Point pos;
	}

	@Override
	protected String doProblem(String[] data) {
		Pieces[][] board = new Pieces[16][16];
		int dataIndex = 0;
		int numPieces = Integer.parseInt(data[dataIndex++]);
		List<PieceOnBoard> pieces = new ArrayList<PieceOnBoard>(numPieces);
		for (; dataIndex <= 3 * numPieces; dataIndex += 3) {
			char piece = data[dataIndex].charAt(0);
			int rank = Integer.parseInt(data[dataIndex + 1]);
			int file = Integer.parseInt(data[dataIndex + 2]);
			board[rank - 1][file - 1] = Pieces.fromId(piece);
			PieceOnBoard pob = new PieceOnBoard();
			pob.p = Pieces.fromId(piece);
			pob.pos = new Point(rank - 1, file - 1);
			pieces.add(pob);
		}
		int danger = 0;

		defs: for (PieceOnBoard def : pieces) {
			for (PieceOnBoard att : pieces) {
				if (def == att) {
					continue;
				}
				if (att.p.isReachable(board, att.pos, def.pos)) {
					danger++;
					continue defs;
				}
			}
		}
		return Integer.toString(danger);
	}

	public static void main(String[] args) throws IOException {
		new Chess2().run(args);
	}

	@Override
	public String getName() {
		return "chess2";
	}

}
