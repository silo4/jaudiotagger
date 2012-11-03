package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTPE2Test extends AbstractTestCase {
	public void testID3Specific() throws Exception {
		Exception e = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			final ID3v23Frame frame = new ID3v23Frame("TPE2");
			frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testband"));
			tag.addFrame(frame);
			assertEquals("testband", tag.getFirst("TPE2"));
		} catch (final Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
		assertNull(e);
	}

}
