package org.jaudiotagger.tag.datatype;

/**
 * A pair
 */
public class Pair {
	private String key;
	private String value;

	public Pair(final String key, final String value) {
		setKey(key);
		setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}
