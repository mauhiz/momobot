package net.mauhiz.board.chess.model;

import net.mauhiz.board.Move;
import net.mauhiz.board.chess.model.ChessBoard.Status;

public class ChessMove extends Move {
    public boolean capture;
    public ChessPiece moved;
    public ChessPiece promotion;
    public Status status;
}
