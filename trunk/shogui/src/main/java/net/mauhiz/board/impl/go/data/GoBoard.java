package net.mauhiz.board.impl.go.data;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.mauhiz.board.impl.common.data.AbstractBoard;
import net.mauhiz.board.model.data.Drop;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.PocketBoard;
import net.mauhiz.board.model.data.Square;

public class GoBoard extends AbstractBoard implements PocketBoard {
    protected final Map<GoPlayerType, List<GoPieceType>> pockets = new HashMap<GoPlayerType, List<GoPieceType>>(
            2);

    public GoBoard() {
        super();
        for (GoPlayerType spt : GoPlayerType.values()) {
            pockets.put(spt, new ArrayList<GoPieceType>());
        }
    }

    public void applyMove(Move move) {
        if (move instanceof Drop) {
            Drop drop = (Drop) move;
            GoPieceType pieceType = (GoPieceType) drop.getPieceType();
            GoPlayerType playerType = (GoPlayerType) drop.getPlayerType();
            pockets.get(playerType).remove(pieceType);
            setPieceAt(drop.getTo(), new GoPiece(playerType, pieceType));
        }
    }

    public Collection<GoPiece> getAllPocketPieces() {
        List<GoPiece> pieces = new ArrayList<GoPiece>();
        for (Entry<GoPlayerType, List<GoPieceType>> ent : pockets.entrySet()) {
            GoPlayerType playerType = ent.getKey();

            for (GoPieceType pt : ent.getValue()) {
                pieces.add(new GoPiece(playerType, pt));
            }
        }

        return pieces;
    }

    @Override
    public GoPiece getPieceAt(Square square) {
        return (GoPiece) super.getPieceAt(square);
    }

    @Override
    public List<GoPieceType> getPocket(PlayerType player) {
        return pockets.get(player);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(19, 19);
    }

    @Override
    public GoPiece setPieceAt(Square square, Piece piece) {
        return (GoPiece) super.setPieceAt(square, piece);
    }
}
