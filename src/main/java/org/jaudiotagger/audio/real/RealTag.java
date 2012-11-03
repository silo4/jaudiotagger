package org.jaudiotagger.audio.real;

import org.jaudiotagger.audio.generic.GenericTag;

public class RealTag extends GenericTag {
	@Override
	public String toString() {
		final String output = "REAL " + super.toString();
		return output;
	}

}
