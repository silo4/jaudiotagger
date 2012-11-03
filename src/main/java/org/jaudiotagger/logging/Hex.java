package org.jaudiotagger.logging;

/**
 * Display as hex
 */
public class Hex {
	/**
	 * Display as hex
	 * 
	 * @param value
	 * @return
	 */
	public static String asHex(final long value) {
		return "0x" + Long.toHexString(value);
	}

	/**
	 * Display as hex
	 * 
	 * @param value
	 * @return
	 */
	public static String asHex(final byte value) {
		return "0x" + Integer.toHexString(value);
	}
}
