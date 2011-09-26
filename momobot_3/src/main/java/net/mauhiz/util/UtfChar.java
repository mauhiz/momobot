package net.mauhiz.util;

public final class UtfChar {
    private static final UtfChar[] ASCII = new UtfChar[0x7f];

    static {
        initAsciiCache();
    }

    public static UtfChar charAt(String str, int index) {
        return valueOf(str.codePointAt(index));
    }

    public static UtfChar charAt(StringBuilder sb, int index) {
        return valueOf(sb.codePointAt(index));
    }

    private static void initAsciiCache() {
        for (int i = 0; i < ASCII.length; i++) {
            ASCII[i] = new UtfChar(i);
        }
    }

    public static UtfChar valueOf(int codePoint) {
        if (codePoint >= 0 && codePoint < ASCII.length) {
            return ASCII[codePoint];
        }
        return new UtfChar(codePoint);
    }

    private final int codePoint;

    private UtfChar(int codePoint) {
        this.codePoint = codePoint;
    }

    public StringBuilder appendTo(StringBuilder sb) {
        return sb.appendCodePoint(codePoint);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof UtfChar && ((UtfChar) obj).codePoint == codePoint;
    }

    public int getCharCount() {
        return Character.charCount(codePoint);
    }

    public int getCodePoint() {
        return codePoint;
    }

    @Override
    public int hashCode() {
        return codePoint;
    }

    public boolean isDigit() {
        return Character.isDigit(codePoint);
    }

    public boolean isEquals(int otherCodePoint) {
        return otherCodePoint == codePoint;
    }

    public boolean isLetter() {
        return Character.isLetter(codePoint);
    }

    public boolean isSpaceChar() {
        return Character.isSpaceChar(codePoint);
    }

    public boolean isUpperCase() {
        return Character.isUpperCase(codePoint);
    }

    public char[] toChars() {
        return Character.toChars(codePoint);
    }

    public UtfChar toLowerCase() {
        return UtfChar.valueOf(Character.toLowerCase(codePoint));
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    public StringBuilder toStringBuilder() {
        return appendTo(new StringBuilder(1));
    }

    public UtfChar toUpperCase() {
        return UtfChar.valueOf(Character.toUpperCase(codePoint));
    }
}