package org.jaudiotagger.tag.id3.framebody;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameBodyUSLTTest extends AbstractTestCase {

	public static final String UTF16_REQUIRED = "\u2026";

	public void testWriteUnicodeBody() throws IOException {
		final FrameBodyUSLT fb = new FrameBodyUSLT(TextEncoding.UTF_16, "eng", "", UTF16_REQUIRED);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fb.write(baos);
		final FileOutputStream fos = new FileOutputStream("TEST.TXT");
		fos.write(baos.toByteArray());
		final byte[] frameBody = baos.toByteArray();
		final byte[] correctBits = makeByteArray(new int[] { 0x01, 'e', 'n', 'g', 0xff, 0xfe, 0x00, 0x00, 0xff, 0xfe, 0x26, 0x20 });
		final String s = cmp(correctBits, frameBody);
		if (s != null)
			fail(s);
	}

	private String cmp(final byte[] a, final byte[] b) {
		if (a.length != b.length)
			return "length of byte arrays differ (" + a.length + "!=" + b.length + ")";
		for (int i = 0; i < a.length; i++)
			if (a[i] != b[i])
				return "byte arrays differ at offset " + i + " (" + a[i] + "!=" + b[i] + ")";
		return null;
	}

	private byte[] makeByteArray(final int[] ints) {
		final byte[] bs = new byte[ints.length];
		for (int i = 0; i < ints.length; i++)
			bs[i] = (byte) ints[i];
		return bs;
	}
}
