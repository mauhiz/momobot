package net.mauhiz.fbook.puzzle;

import java.math.BigInteger;

/**
 * see http://www.davideisenstat.com/fbpfaq/
 * @author mauhiz
 */
public class FindEmail {

	/**
	 * note : bit operation on long underflows.
	 */
	public static void main(String[] args) {
		BigInteger source = new BigInteger("FACEB00C", 16);
		BigInteger result = source.shiftRight(2);
		System.out.println(result.toString());
	}

}
