package net.mauhiz.shogi.model;

import static java.lang.Math.abs;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Board {
    public static final int SIZE = 9;
    static int getXmove(Point from, Point to) {
        return to.x - from.x;
    }
    
    static int getYmove(Point from, Point to) {
        return to.y - from.y;
    }
    
    static boolean isCorner(Point from, Point to) {
        return abs(getXmove(from, to)) == 1 && abs(getYmove(from, to)) == 1;
    }
    
    static boolean isCross(Point from, Point to) {
        return abs(getXmove(from, to)) == 1 ^ abs(getYmove(from, to)) == 1;
    }
    
    static boolean isDiagonal(Point from, Point to) {
        return abs(getXmove(from, to)) == abs(getYmove(from, to));
    }
    
    static boolean isDownToUp(Point from, Point to) {
        return to.y > from.y;
    }
    
    static boolean isFrontCorner(Point from, Point to, Player pl) {
        return abs(getXmove(from, to)) == 1 && getYmove(from, to) == (pl == Player.BLACK ? 1 : -1);
    }
    
    static boolean isHorizontal(Point from, Point to) {
        return getYmove(from, to) == 0;
    }
    
    static boolean isLeftToRight(Point from, Point to) {
        return to.x > from.x;
    }
    
    static boolean isStraight(Point from, Point to) {
        return isHorizontal(from, to) || isVertical(from, to);
    }
    
    static boolean isVertical(Point from, Point to) {
        return getXmove(from, to) == 0;
    }
    
    private final Map<Player, Set<OwnedPiece>> pockets = new HashMap<Player, Set<OwnedPiece>>();
    private final OwnedPiece[][] squares = new OwnedPiece[SIZE][SIZE];
    
    private Player turn = Player.BLACK;
    {
        pockets.put(Player.WHITE, new HashSet<OwnedPiece>());
        pockets.put(Player.BLACK, new HashSet<OwnedPiece>());
    }
    
    public boolean drop(Player pl, Piece p, Point to) {
        if (pl != turn || !Rule.canDrop(this, to)) {
            return false;
        }
        
        OwnedPiece toDrop = null;
        // look in pockets
        for (Iterator<OwnedPiece> inPocket = pockets.get(pl).iterator(); inPocket.hasNext();) {
            OwnedPiece op = inPocket.next();
            
            if (op.piece == p) {
                inPocket.remove();
                toDrop = op;
                break;
            }
        }
        
        if (toDrop != null) {
            squares[to.x][to.y] = toDrop;
            turn = turn.other();
            return true;
        }
        return false;
    }
    
    public OwnedPiece getOwnedPieceAt(int i, int j) {
        return squares[i][j];
    }
    
    public OwnedPiece getOwnedPieceAt(Point targetPoint) {
        return getOwnedPieceAt(targetPoint.x, targetPoint.y);
    }
    
    public Player getTurn() {
        return turn;
    }
    
    public Player isCheck() {
        return null; // TODO later
    }
    
    public boolean isFriendlyPieceOn(Player pl, Point to) {
        OwnedPiece op = squares[to.x][to.y];
        return op != null && op.player == pl;
    }
    
    public boolean isObstruction(Point from, Point to) {
        if (isDiagonal(from, to)) {
            boolean positiveCoef = isDownToUp(from, to) ^ !isLeftToRight(from, to);
            int minX = Math.min(from.x, to.x);
            int maxX = Math.max(from.x, to.x);
            
            int minY = Math.min(from.y, to.y);
            int maxY = Math.max(from.y, to.y);
            
            for (int i = 1; i < maxX - minX; i++) {
                if (getOwnedPieceAt(minX + i, positiveCoef ? minY + i : maxY - i) != null) {
                    return true;
                }
            }
            return false;
        } else if (isHorizontal(from, to)) {
            int horIncr = isLeftToRight(from, to) ? 1 : -1;
            for (int i = from.x + horIncr; i != to.x; i += horIncr) {
                if (getOwnedPieceAt(i, to.y) != null) {
                    return true;
                }
            }
            return false;
        } else if (isVertical(from, to)) {
            int vertIncr = isDownToUp(from, to) ? 1 : -1;
            for (int j = from.y + vertIncr; j != to.y; j += vertIncr) {
                if (getOwnedPieceAt(to.x, j) != null) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    
    /**
     * @param from
     * @param to
     * @return true if move was allowed
     */
    public boolean move(Player pl, Point from, Point to) {
        if (pl != turn) {
            return false;
        }
        OwnedPiece toMove = getOwnedPieceAt(from.x, from.y);
        if (toMove == null || toMove.player != pl) {
            return false;
        }
        
        if (Rule.canGo(this, toMove, from, to)) {
            OwnedPiece capturedPiece = getOwnedPieceAt(to.x, to.y);
            
            if (capturedPiece != null) {
                capturedPiece.promoted = false;
                capturedPiece.player = pl;
                pockets.get(pl).add(capturedPiece);
            }
            
            squares[from.x][from.y] = null;
            squares[to.x][to.y] = toMove;
            turn = turn.other();
            return true;
        }
        
        return false;
    }
    
    public void newGame() {
        for (Set<OwnedPiece> pocket : pockets.values()) {
            pocket.clear();
        }
        OwnedPiece.resetId();
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                squares[i][j] = null;
                Player pl = j <= 2 ? Player.BLACK : Player.WHITE;
                
                if (j == 0 || j == 8) {
                    if (i == 0 || i == 8) {
                        squares[i][j] = new OwnedPiece(pl, Piece.LANCE);
                    } else if (i == 1 || i == 7) {
                        squares[i][j] = new OwnedPiece(pl, Piece.KNIGHT);
                    } else if (i == 2 || i == 6) {
                        squares[i][j] = new OwnedPiece(pl, Piece.SILVER);
                    } else if (i == 3 || i == 5) {
                        squares[i][j] = new OwnedPiece(pl, Piece.GOLD);
                    } else {
                        squares[i][j] = new OwnedPiece(pl, Piece.KING);
                    }
                } else if (j == 1 && i == 1 || j == 7 && i == 7) {
                    squares[i][j] = new OwnedPiece(pl, Piece.BISHOP);
                } else if (j == 1 && i == 7 || j == 7 && i == 1) {
                    squares[i][j] = new OwnedPiece(pl, Piece.ROOK);
                } else if (j == 2 || j == 6) {
                    squares[i][j] = new OwnedPiece(pl, Piece.PAWN);
                }
            }
        }
        
        turn = Player.BLACK;
    }
}
