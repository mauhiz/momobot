package net.mauhiz.board.shogi.model;

import net.mauhiz.board.Piece;

public enum ShogiPiece implements Piece {
    BISHOP("角") {
        @Override
        public String getPromotedKanji() {
            return "馬";
        }
    },
    GOLD("金") {
        @Override
        public String getPromotedKanji() {
            throw new IllegalStateException();
        }
    },
    KING("王") {
        @Override
        public String getPromotedKanji() {
            throw new IllegalStateException();
        }
    },
    KNIGHT("桂") {
        @Override
        public String getPromotedKanji() {
            return "圭";
        }
    },
    LANCE("香") {
        @Override
        public String getPromotedKanji() {
            return "杏 ";
        }
    },
    PAWN("歩") {
        @Override
        public String getPromotedKanji() {
            return "と";
        }
    },
    ROOK("飛") {
        @Override
        public String getPromotedKanji() {
            return "龍";
        }
    },
    SILVER("銀") {
        @Override
        public String getPromotedKanji() {
            return "全";
        }
    };
    private String kanji;

    private ShogiPiece(String kanji) {
        this.kanji = kanji;
    }

    public String getName() {
        return kanji;
    }

    public abstract String getPromotedKanji();
}
