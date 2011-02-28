package net.mauhiz.board;

import java.io.Externalizable;

public interface Move extends Externalizable {

    Square getTo();
}
