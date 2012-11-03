package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import org.jaudiotagger.audio.generic.Utils;

public class FormatVersionChunk extends Chunk {

	private final AiffAudioHeader aiffHeader;

	/**
	 * Constructor.
	 * 
	 * @param hdr
	 *            The header for this chunk
	 * @param raf
	 *            The file from which the AIFF data are being read
	 * @param tag
	 *            The AiffTag into which information is stored
	 */
	public FormatVersionChunk(final ChunkHeader hdr, final RandomAccessFile raf, final AiffAudioHeader aHdr) {
		super(raf, hdr);
		aiffHeader = aHdr;
	}

	/**
	 * Reads a chunk and extracts information.
	 * 
	 * @return <code>false</code> if the chunk is structurally invalid, otherwise <code>true</code>
	 */
	@Override
	public boolean readChunk() throws IOException {
		final long rawTimestamp = Utils.readUint32(raf);
		// The timestamp is in seconds since January 1, 1904.
		// We must convert to Java time.
		final Date timestamp = AiffUtil.timestampToDate(rawTimestamp);
		aiffHeader.setTimestamp(timestamp);
		return true;
	}

}
