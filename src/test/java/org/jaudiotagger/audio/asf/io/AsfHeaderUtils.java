package org.jaudiotagger.audio.asf.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ChunkContainer;
import org.jaudiotagger.audio.asf.data.ContainerType;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.MetadataContainer;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * 
 * @author Christian Laireiter
 */
public final class AsfHeaderUtils extends TestCase {

	public final static int BINARY_PRINT_COLUMNS = 20;

	public static String binary2ByteArrayString(final byte[] data) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; data != null && i < data.length; i++) {
			if (i > 0 && i % BINARY_PRINT_COLUMNS == 0)
				result.append(Utils.LINE_SEPARATOR);
			result.append("0x");
			String hex = Integer.toHexString(data[i] & 0xFF);
			if (hex.length() == 1)
				hex = "0" + hex;
			result.append(hex);
			if (i < data.length - 1)
				result.append(',');
		}
		return result.toString();
	}

	public static Chunk findChunk(final Collection<Chunk> chunk, final GUID chunkGUID) {
		Chunk result = null;
		for (final Chunk curr : chunk)
			if (curr instanceof ChunkContainer) {
				result = findChunk(((ChunkContainer) curr).getChunks(), chunkGUID);
				if (result != null)
					break;
			} else if (curr.getGuid().equals(chunkGUID)) {
				result = curr;
				break;
			}
		return result;
	}

	public static byte[] getFirstChunk(final Path file, final GUID chunkGUID) throws IOException {
		RandomAccessFile asfFile = null;
		try {
			asfFile = new RandomAccessFile(file.toFile(), "r");
			byte[] result = new byte[0];
			final AsfHeader readHeader = AsfHeaderReader.readHeader(asfFile);
			final Chunk found = findChunk(readHeader.getChunks(), chunkGUID);
			if (found != null) {
				final byte[] tmp = new byte[(int) found.getChunkLength().longValue()];
				asfFile.seek(found.getPosition());
				asfFile.readFully(tmp);
				result = tmp;
			}
			return result;
		} finally {
			asfFile.close();
		}
	}

	public static MetadataContainer readContainer(final Path file, final ContainerType type) throws IOException {
		final AsfHeader readHeader = AsfHeaderReader.readHeader(file);
		return readHeader.findMetadataContainer(type);
	}

	/**
	 * Test date conversion
	 */
	// TODO we dont know this is correct because need an independent way of checking our figures with an ASF file,
	// the previous calculation appeard incorrect.
	public void testDateHeaderConversion() {
		final Calendar cal = org.jaudiotagger.audio.asf.util.Utils.getDateOf(BigInteger.valueOf(1964448000));
		System.out.println(cal.getTime());
		assertEquals(-11644273555200l, cal.getTimeInMillis());
	}

	/**
	 * Test to show the calculation done to derive the DIFF_BETWEEN_ASF_DATE_AND_JAVA_DATE constant
	 */
	public void testConversionDateConstant() {
		final Date date1 = new Date((1601 - 1900), 0, 1);
		final Date date2 = new Date((1970 - 1900), 0, 1);
		assertEquals(11644470000000l, date2.getTime() - date1.getTime());
	}

}
