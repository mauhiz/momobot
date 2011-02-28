package net.mauhiz.board;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class AbstractMove<P extends Piece, J extends Player> implements Move {
    protected Square from;
    protected Square to;

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        from = Square.getInstance(in.readInt(), in.readInt());
        to = Square.getInstance(in.readInt(), in.readInt());
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public void setTo(Square to) {
        this.to = to;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(from.x);
        out.writeInt(from.y);
        out.writeInt(to.x);
        out.writeInt(to.y);
    }
}
