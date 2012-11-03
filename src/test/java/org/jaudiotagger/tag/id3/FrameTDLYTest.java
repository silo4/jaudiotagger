package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDLY;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

public class FrameTDLYTest extends AbstractTestCase {
	public void testID3Specific() throws Exception {
		Exception e = null;
		try {
			final ID3v24Tag tag = new ID3v24Tag();
			final ID3v24Frame frame = new ID3v24Frame("TDLY");
			frame.setBody(new FrameBodyTDLY(TextEncoding.ISO_8859_1, "11:10"));
			tag.addFrame(frame);
			assertEquals("11:10", tag.getFirst("TDLY"));
		} catch (final Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
		assertNull(e);
	}

}
