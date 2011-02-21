package net.mauhiz.shogi.model;

public class OwnedPiece {
    private static int idCounter;
    private static int incrId() {
        return ++idCounter;
    }
    public static void resetId() {
        idCounter = 0;
    }
    
    private final int id;
    
    public Piece piece;
    public Player player;
    public boolean promoted;
    
    public OwnedPiece(Player player, Piece piece) {
        id = incrId();
        this.piece = piece;
        this.player = player;
    }
    
    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof OwnedPiece && obj.hashCode() == hashCode();
    }
    
    public String getSymbol() {
        String symbol = piece.name().substring(0, 2);
        return promoted ? "[" + symbol + "]" : symbol;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
}
