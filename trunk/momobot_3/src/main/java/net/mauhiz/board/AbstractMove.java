package net.mauhiz.board;

public abstract class AbstractMove<B extends Board> implements Move<B> {
    protected Square from;
    private Player player;
    protected Square to;

    public Square getFrom() {
        return from;
    }

    public Player getPlayer() {
        return player;
    }

    public Square getTo() {
        return to;
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTo(Square to) {
        this.to = to;
    }
}
