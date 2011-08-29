package net.mauhiz.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author mauhiz
 */
public enum NetUtils {
    ;

    /**
     * @param address
     *            the byte[] of size 4 representing the IP address.
     * @return a long representation of the IP address.
     */
    private static long byteTabToLong(byte[] address) {
        ByteBuffer buf = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);

        // if IP is IPv4, we need to add trailing zeroes
        for (int i = address.length; i < buf.capacity(); i++) {
            buf.put((byte) 0);
        }

        buf.put(address).rewind();
        return buf.getLong();
    }

    public static boolean checkPortRange(SocketAddress sa, int minPort, int maxPort) {
        if (sa instanceof InetSocketAddress) {
            int port = ((InetSocketAddress) sa).getPort();
            return port >= minPort && port <= maxPort;
        }
        return false;
    }

    public static String doHttpGet(String url) throws IOException, URISyntaxException {
        return doHttpGet(new URI(url));
    }

    public static String doHttpGet(URI url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpResponse resp = new DefaultHttpClient().execute(get);
        return IOUtils.toString(resp.getEntity().getContent());
    }

    /**
     * @param address
     *            l'adresse
     * @return un kikoo
     */
    public static long iaToLong(InetAddress address) {
        return byteTabToLong(address.getAddress());
    }

    /**
     * A convenient method that accepts an IP address represented as a long and returns an integer array of size 4
     * representing the same IP address.
     * 
     * @param address
     *            the long value representing the IP address.
     */
    private static ByteBuffer longToByteTab(long address) {
        ByteBuffer buf = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        buf.putLong(address).rewind();
        if (buf.getInt() == 0) { // this IP is IPv4, we need to remove trailing zeroes
            return ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).put(buf);
        }
        return buf;
    }

    /**
     * @param longip
     *            une ip
     * @return une inetaddress
     */
    public static InetAddress longToIa(long longip) throws UnknownHostException {
        return InetAddress.getByAddress(longToByteTab(longip).array());
    }

    /**
     * @param str
     *            la chaine a parser
     * @return ?
     */
    public static SocketAddress makeISA(String str) {
        String[] parts = StringUtils.split(str, ':');
        if (ArrayUtils.getLength(parts) == 2) {
            int port = Integer.parseInt(parts[1]);
            return new InetSocketAddress(parts[0], port);
        }
        throw new IllegalArgumentException("The address and the port must be separated by a dot.");
    }
}
