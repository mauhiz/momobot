package net.mauhiz.board;

import java.io.Externalizable;

public interface Move<B extends Board> extends Externalizable {

    Square getTo();
}
