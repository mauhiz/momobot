package net.mauhiz.board.impl.shogi.data;

import net.mauhiz.board.model.data.PieceType;

public enum ShogiPieceType implements PieceType {
	BISHOP("角") {
		@Override
		public ShogiPieceType getPromotion() {
			return HORSE;
		}
	},
	DRAGON("龍") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	GOLD("金") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	HORSE("馬") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	KING("王") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	KNIGHT("桂") {
		@Override
		public ShogiPieceType getPromotion() {
			return PROMOTED_KNIGHT;
		}
	},
	LANCE("香") {
		@Override
		public ShogiPieceType getPromotion() {
			return PROMOTED_LANCE;
		}
	},
	PAWN("歩") {
		@Override
		public ShogiPieceType getPromotion() {
			return TOKIN;
		}
	},
	PROMOTED_KNIGHT("圭") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	PROMOTED_LANCE("杏") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	PROMOTED_SILVER("全") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	},
	ROOK("飛") {
		@Override
		public ShogiPieceType getPromotion() {
			return DRAGON;
		}
	},
	SILVER("銀") {
		@Override
		public ShogiPieceType getPromotion() {
			return PROMOTED_SILVER;
		}
	},
	TOKIN("と") {
		@Override
		public ShogiPieceType getPromotion() {
			return null;
		}
	};

	public static ShogiPieceType fromKanji(char piece) {
		for (ShogiPieceType spt : values()) {
			if (spt.kanji.charAt(0) == piece) {
				return spt;
			}
		}
		return null;
	}

	private String kanji;

	private ShogiPieceType(String kanji) {
		this.kanji = kanji;
	}

	public boolean canPromote() {
		return getPromotion() != null;
	}

	public String getName() {
		return kanji;
	}

	public abstract ShogiPieceType getPromotion();

	public ShogiPieceType reversePromotion() {
		switch (this) {
		case TOKIN:
			return PAWN;
		case PROMOTED_KNIGHT:
			return KNIGHT;
		case PROMOTED_LANCE:
			return LANCE;
		case PROMOTED_SILVER:
			return SILVER;
		case HORSE:
			return BISHOP;
		case DRAGON:
			return ROOK;
		default:
			return this;
		}
	}

	@Override
	public String toString() {
		return kanji;
	}
}
