package org.jaudiotagger.tag;

/**
 * Thrown if the key cannot be found
 * <p/>
 * <p>
 * Should not happen with well written code, hence RuntimeException.
 */
public class KeyNotFoundException extends RuntimeException {
	/**
	 * Creates a new KeyNotFoundException datatype.
	 */
	public KeyNotFoundException() {}

	/**
	 * Creates a new KeyNotFoundException datatype.
	 * 
	 * @param ex
	 *            the cause.
	 */
	public KeyNotFoundException(final Throwable ex) {
		super(ex);
	}

	/**
	 * Creates a new KeyNotFoundException datatype.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public KeyNotFoundException(final String msg) {
		super(msg);
	}

	/**
	 * Creates a new KeyNotFoundException datatype.
	 * 
	 * @param msg
	 *            the detail message.
	 * @param ex
	 *            the cause.
	 */
	public KeyNotFoundException(final String msg, final Throwable ex) {
		super(msg, ex);
	}
}
