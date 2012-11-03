package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frames;

/**
 * Test RVADFrameBody
 */
public class FrameBodyRVADTest extends AbstractTestCase {
	public static byte[] TEST_BYTES;

	static {
		TEST_BYTES = FrameBodyRVADTest.makeByteArray(new int[] { 0x03, 0x04 });
	}

	public static FrameBodyRVAD getInitialisedBody() {
		final FrameBodyRVAD fb = new FrameBodyRVAD();
		fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES);
		return fb;
	}

	private static byte[] makeByteArray(final int[] ints) {
		final byte[] bs = new byte[ints.length];
		for (int i = 0; i < ints.length; i++)
			bs[i] = (byte) ints[i];
		return bs;
	}

	public void testCreateFrameBody() {
		Exception exceptionCaught = null;
		FrameBodyRVAD fb = null;
		try {
			fb = new FrameBodyRVAD();
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
		assertEquals(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT, fb.getIdentifier());

	}

	public void testCreateFrameBodyEmptyConstructor() {
		Exception exceptionCaught = null;
		FrameBodyRVAD fb = null;
		try {
			fb = new FrameBodyRVAD();
			fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES);
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
		assertEquals(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT, fb.getIdentifier());
		assertEquals(TEST_BYTES, fb.getObjectValue(DataTypes.OBJ_DATA));

	}

}
