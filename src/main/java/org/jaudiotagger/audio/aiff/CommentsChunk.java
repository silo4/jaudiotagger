package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import org.jaudiotagger.audio.generic.Utils;

public class CommentsChunk extends Chunk {

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
	public CommentsChunk(final ChunkHeader hdr, final RandomAccessFile raf, final AiffAudioHeader aHdr) {
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
		final int numComments = Utils.readUint16(raf);
		// Create a List of comments
		for (int i = 0; i < numComments; i++) {
			final long timestamp = Utils.readUint32(raf);
			final Date jTimestamp = AiffUtil.timestampToDate(timestamp);
			final int marker = Utils.readInt16(raf);
			final int count = Utils.readUint16(raf);
			bytesLeft -= 8;
			final byte[] buf = new byte[count];
			raf.read(buf);
			bytesLeft -= count;
			String cmt = new String(buf);

			// Append a timestamp to the comment
			cmt += " " + AiffUtil.formatDate(jTimestamp);
			aiffHeader.addComment(cmt);
		}
		return true;
	}

}
