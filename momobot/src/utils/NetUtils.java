package utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * @author Administrator
 */
public abstract class NetUtils {
    /**
     * nombre de champs dans l'Ipv4.
     */
    static final int IP_FIELDS = 4;

    /**
     * Une IP vaut 32 bits, un int aussi. Par contre l'entier est signé...
     * @param addr
     *            l'adresse
     * @return ?
     */
    static int bytesToInt(final byte[] addr) {
        if (addr.length != IP_FIELDS) {
            return 0;
        }
        int retour = 0;
        for (int i = 0; i < addr.length; i++) {
            retour |= Byte.valueOf(addr[i]).intValue() << Byte.SIZE
                    * (addr.length - i - 1);
        }
        return retour;
    }

    /**
     * A convenient method that accepts an IP address represented by a byte[] of
     * size 4 and returns this as a long representation of the same IP address.
     * @since PircBot 0.9.4
     * @param address
     *            the byte[] of size 4 representing the IP address.
     * @return a long representation of the IP address.
     */
    public static long byteTabIpToLong(final byte[] address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("byte array must be of length 4");
        }
        long ipNum = 0;
        long multiplier = 1;
        for (int i = address.length - 1; i >= 0; i--) {
            final int byteVal = (address[i] + 0x100) % 0x100;
            ipNum += byteVal * multiplier;
            multiplier *= 0x100;
        }
        return ipNum;
    }

    /**
     * @param ip
     *            l'IP
     * @return ?
     */
    static InetAddress intTabToIp(final int[] ip) {
        final StrBuilder name = new StrBuilder();
        name.appendWithSeparators(ArrayUtils.toObject(ip), ".");
        try {
            return InetAddress.getByName(name.toString());
        } catch (final UnknownHostException e) {
            Utils.logError(NetUtils.class, e);
            return null;
        }
    }

    /**
     * @param addr
     *            l'adresse
     * @return ?
     */
    static byte[] intToBytes(final int addr) {
        final byte[] result = new byte[IP_FIELDS];
        for (int i = 0; i < result.length; i++) {
            final int mask = Byte.MAX_VALUE + Byte.MIN_VALUE << Byte.SIZE
                    * (IP_FIELDS - i - 1);
            result[i] = (byte) ((addr & mask) >> Byte.SIZE
                    * (IP_FIELDS - i - 1));
        }
        return result;
    }

    /**
     * @param ipay
     * @param game
     * @return une collection de players
     */
    // public static Collection<Player> getPlayers(InetSocketAddress ipay,
    // String game) {
    // byte[] buf = getInfo(ipay, "cs", "players").getBytes();
    // if (buf.length <= 6) return null;
    // if (buf[0] != buf[1] || buf[1] != buf[2] || buf[2] != buf[3] || buf[4] !=
    // 'D') return null;
    // Collection<Player> sorted = new ArrayList<Player>();
    // int playerCount = buf[5] & 255;
    // String[] playerNames = new String[playerCount];
    // int[] playerFrags = new int[playerCount];
    // int off = 6;
    // for (int i = 0; i < playerCount; ++i) {
    // Player player = new Player("");
    // StringBuffer playerName = new StringBuffer(20);
    // while (buf[off] != 0) {
    // playerName.append((char) (buf[off++] & 255));
    // }
    // off++;
    // player.setName(playerName.toString().trim());
    // player.setFrags((buf[off] & 255) | ((buf[off + 1] & 255) << 8) |
    // ((buf[off + 2] & 255) << 16)
    // | ((buf[off + 3] & 255) << 24));
    // sorted.add(player);
    // off += 8;
    // }
    // Collections.sort(sorted, new KillsComparator());
    // return sorted;
    // }
    /**
     * @param address
     *            l'adresse
     * @return un kikoo
     */
    public static long ipToLong(final InetAddress address) {
        final byte[] ip = address.getAddress();
        long ipNum = 0;
        long multiplier = 1;
        int byteVal;
        for (int i = ip.length - 1; i >= 0; i--) {
            byteVal = (ip[i] + 0x100) % 0x100;
            ipNum += byteVal * multiplier;
            multiplier *= 0x100;
        }
        return ipNum;
    }

    /**
     * A convenient method that accepts an IP address represented as a long and
     * returns an integer array of size 4 representing the same IP address.
     * @param address1
     *            the long value representing the IP address.
     * @return An int[] of size 4.
     */
    public static int[] longToIntTab(final long address1) {
        long address = address1;
        final int[] ip = new int[IP_FIELDS];
        for (int i = ip.length - 1; i >= 0; i--) {
            ip[i] = (int) (address % 0x100);
            address = address / 0x100;
        }
        return ip;
    }

    /**
     * @param longip
     *            une ip
     * @return une inetaddress
     */
    public static InetAddress longToIp(final long longip) {
        return intTabToIp(longToIntTab(longip));
    }

    /**
     * @param s
     *            la chaine à parser
     * @return ?
     */
    public static InetSocketAddress makeISA(final String s) {
        final int i = s.indexOf(':');
        if (i > -1) {
            try {
                final InetAddress ip = InetAddress.getByName(s.substring(0, i));
                final int port = Integer.parseInt(s.substring(i + 1));
                return new InetSocketAddress(ip, port);
            } catch (final IllegalArgumentException e) {
                Utils.logError(Utils.class, e);
            } catch (final IOException e) {
                Utils.logError(Utils.class, e);
            }
        }
        return null;
    }

    /**
     * @param uns
     *            un long non signé
     * @return un tableau d'octets qui représente un entier non signé
     */
    public static byte[] unsignedIntegerToByteArray(final long uns) {
        final byte[] retour = new byte[Integer.SIZE / Byte.SIZE];
        int shift = Integer.SIZE;
        final int mask = Byte.MIN_VALUE + Byte.MAX_VALUE;
        for (int i = 0; i < retour.length; i++) {
            shift -= Byte.SIZE;
            retour[i] = (byte) (uns >> shift & mask);
        }
        return retour;
    }

    /**
     * constructeur caché.
     */
    protected NetUtils() {
        throw new UnsupportedOperationException();
    }
}
