package net.mauhiz.util;

import java.text.Normalizer;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public enum StringUtil {
    ;
    private static final Pattern NOT_ASCII = Pattern.compile("[^\\p{ASCII}]");
    private static final Random RANDOM = new Random();

    /**
     * Based on commons-lang's version
     */
    public static boolean containsAny(String cs, Collection<UtfChar> searchChars) {
        if (StringUtils.isEmpty(cs) || CollectionUtils.isEmpty(searchChars)) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            UtfChar ch = UtfChar.charAt(cs, i);
            if (searchChars.contains(ch)) {
                return true;
            }
        }
        return false;
    }

    public static String normalizeToAscii(String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return NOT_ASCII.matcher(temp).replaceAll("");
    }

    public static String remove(String line, UtfChar[] utfChars) {
        StringBuilder ret = new StringBuilder(line.length());
        for (int i = 0; i < line.length(); i++) {
            UtfChar uc = UtfChar.charAt(line, i);
            if (!ArrayUtils.contains(utfChars, uc)) {
                uc.appendTo(ret);
            }
        }
        return ret.toString();
    }

    public static void removeCharAt(StringBuilder sb, int index) {
        int charCount = UtfChar.charAt(sb, index).getCharCount();
        sb.delete(index, index + charCount);
    }

    public static void setUtfChar(StringBuilder sb, int index, UtfChar utfChar) {
        char[] chrs = utfChar.toChars();
        sb.setCharAt(index, chrs[0]);
        for (int i = 1; i < chrs.length; i++) {
            sb.insert(index + i, chrs[i]);
        }
    }

    /**
     * @param seq
     *            une chaine a shaker
     * @return la chaine randomisee
     */
    public static StringBuilder utf8Shuffle(String seq) {
        StringBuilder input = new StringBuilder(seq);
        StringBuilder output = new StringBuilder(seq.length());
        for (int len = input.length(); len > 0; len--) {
            int random = RANDOM.nextInt(len);
            UtfChar.charAt(input, random).appendTo(output);
            removeCharAt(input, random);
        }
        return output;
    }
}
