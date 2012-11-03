package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;

/**
 * Test RVA2FrameBody
 */
public class FrameBodyRVA2Test extends AbstractTestCase {
	public static byte[] TEST_BYTES;

	static {
		TEST_BYTES = FrameBodyRVA2Test.makeByteArray(new int[] { 0x01, 0x2 });
	}

	public static FrameBodyRVA2 getInitialisedBody() {
		final FrameBodyRVA2 fb = new FrameBodyRVA2();

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
		FrameBodyRVA2 fb = null;
		try {
			fb = new FrameBodyRVA2();
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);

	}

	public void testCreateFrameBodyEmptyConstructor() {

		Exception exceptionCaught = null;
		FrameBodyRVA2 fb = null;
		try {
			fb = new FrameBodyRVA2();
			fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES);
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
		assertEquals(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2, fb.getIdentifier());
		assertEquals(TEST_BYTES, fb.getObjectValue(DataTypes.OBJ_DATA));

	}

}
