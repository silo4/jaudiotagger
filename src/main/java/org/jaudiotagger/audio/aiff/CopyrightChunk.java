package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CopyrightChunk extends TextChunk {

	private final AiffAudioHeader aiffHeader;

	/**
	 * Constructor.
	 * 
	 * @param hdr
	 *            The header for this chunk
	 * @param raf
	 *            The file from which the AIFF data are being read
	 * @param aHdr
	 *            The AiffAudioHeader into which information is stored
	 */
	public CopyrightChunk(final ChunkHeader hdr, final RandomAccessFile raf, final AiffAudioHeader aHdr) {
		super(hdr, raf);
		aiffHeader = aHdr;
	}

	@Override
	public boolean readChunk() throws IOException {
		if (!super.readChunk())
			return false;
		aiffHeader.setCopyright(chunkText);
		return true;
	}

}
