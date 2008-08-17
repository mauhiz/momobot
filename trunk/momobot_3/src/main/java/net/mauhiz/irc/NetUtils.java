package net.mauhiz.irc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class NetUtils {
    /**
     * masque pour signer
     */
    static final int BYTE_MASK = -1;
    /**
     * 256.
     */
    public static final int IP_FIELD_RANGE = 0x100;
    /**
     * nombre de champs dans l'Ipv4.
     */
    public static final int IP_FIELDS = 4;
    
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(NetUtils.class);
    
    /**
     * A convenient method that accepts an IP address represented by a byte[] of size 4 and returns this as a long
     * representation of the same IP address.
     * 
     * @since PircBot 0.9.4
     * @param address
     *            the byte[] of size 4 representing the IP address.
     * @return a long representation of the IP address.
     */
    public static long byteTabToLong(final byte[] address) {
        if (address.length != IP_FIELDS) {
            throw new IllegalArgumentException("byte array must be of length " + IP_FIELDS);
        }
        long ipNum = 0;
        long multiplier = 1;
        for (int i = address.length - 1; i >= 0; --i) {
            final int byteVal = (address[i] + IP_FIELD_RANGE) % IP_FIELD_RANGE;
            ipNum += byteVal * multiplier;
            multiplier *= IP_FIELD_RANGE;
        }
        return ipNum;
    }
    
    /**
     * Une IP vaut 32 bits, un int aussi. Par contre l'entier est signé...
     * 
     * @param addr
     *            l'adresse
     * @return ?
     */
    public static int byteTabToSignedInt(final byte[] addr) {
        return (int) byteTabToLong(addr);
    }
    
    /**
     * @param ip
     *            l'IP
     * @return ?
     */
    public static InetAddress charTabToIa(final char[] ip) {
        if (ip.length != IP_FIELDS) {
            throw new IllegalArgumentException("short array must be of length " + IP_FIELDS);
        }
        try {
            StrBuilder name = new StrBuilder();
            for (char ipField : ip) {
                name.append('.');
                name.append((int) ipField);
            }
            return InetAddress.getByName(name.substring(1));
        } catch (final UnknownHostException uhe) {
            LOG.warn(uhe, uhe);
        }
        return null;
    }
    
    /**
     * @param address
     *            l'adresse
     * @return un kikoo
     */
    public static long iaToLong(final InetAddress address) {
        final byte[] ip = address.getAddress();
        long ipNum = 0;
        long multiplier = 1;
        int byteVal;
        for (int i = IP_FIELDS - 1; i >= 0; --i) {
            /* byte is unsigned in IP */
            byteVal = (ip[i] + IP_FIELD_RANGE) % IP_FIELD_RANGE;
            ipNum += byteVal * multiplier;
            multiplier *= IP_FIELD_RANGE;
        }
        return ipNum;
    }
    
    /**
     * @param addr
     *            l'adresse
     * @return ?
     */
    public static byte[] intToBytes(final int addr) {
        final byte[] result = new byte[IP_FIELDS];
        for (int i = 0; i < result.length; ++i) {
            final int lmask = BYTE_MASK << Byte.SIZE * (IP_FIELDS - i - 1);
            result[i] = (byte) ((addr & lmask) >> Byte.SIZE * (IP_FIELDS - i - 1));
        }
        return result;
    }
    
    /**
     * A convenient method that accepts an IP address represented as a long and returns an integer array of size 4
     * representing the same IP address.
     * 
     * @param address1
     *            the long value representing the IP address.
     * @return A short[] of size 4.
     */
    public static char[] longToCharTab(final long address1) {
        long address = address1;
        final char[] ip = new char[IP_FIELDS];
        for (int i = IP_FIELDS - 1; i >= 0; --i) {
            ip[i] = (char) (address % IP_FIELD_RANGE);
            address /= IP_FIELD_RANGE;
        }
        return ip;
    }
    
    /**
     * @param longip
     *            une ip
     * @return une inetaddress
     */
    public static InetAddress longToIa(final long longip) {
        return charTabToIa(longToCharTab(longip));
    }
    /**
     * @param str
     *            la chaine à parser
     * @return ?
     */
    public static InetSocketAddress makeISA(final String str) {
        final int index = str.indexOf(':');
        if (index > -1) {
            try {
                final InetAddress ip = InetAddress.getByName(str.substring(0, index));
                final int port = Integer.parseInt(str.substring(index + 1));
                return new InetSocketAddress(ip, port);
            } catch (final IOException ioe) {
                LOG.warn(ioe, ioe);
            } catch (final NumberFormatException nfe) {
                LOG.warn(nfe, nfe);
            }
        }
        return null;
    }
    /**
     * @param uns
     *            un long non signé
     * @return un tableau d'octets qui représente un entier non signé
     */
    public static byte[] unsIntToByteTab(final long uns) {
        final byte[] retour = new byte[Integer.SIZE / Byte.SIZE];
        int shift = Integer.SIZE;
        
        for (char i = 0; i < retour.length; ++i) {
            shift -= Byte.SIZE;
            retour[i] = (byte) (uns >> shift & BYTE_MASK);
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
