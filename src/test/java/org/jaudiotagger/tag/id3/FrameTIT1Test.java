package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTIT1Test extends AbstractTestCase {
	public void testID3Specific() throws Exception {
		Exception e = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			final ID3v23Frame frame = new ID3v23Frame("TIT1");
			frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testgrouping"));
			tag.addFrame(frame);
			assertEquals("testgrouping", tag.getFirst("TIT1"));
		} catch (final Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
		assertNull(e);
	}

}
