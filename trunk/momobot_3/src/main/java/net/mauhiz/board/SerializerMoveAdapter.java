package net.mauhiz.board;

import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.SerializationUtils;

/**
 * Default serialization for moves
 * @author mauhiz
 */
public class SerializerMoveAdapter<B extends Board, M extends Move<B>> implements MoveReader<B, M>, MoveWriter<B, M> {

    @Override
    public M read(B board, String moveStr) {
        return (M) SerializationUtils.deserialize(moveStr.getBytes(FileUtil.UTF8));
    }

    @Override
    public String write(B board, M move) {
        return new String(SerializationUtils.serialize(move), FileUtil.UTF8);
    }

}
