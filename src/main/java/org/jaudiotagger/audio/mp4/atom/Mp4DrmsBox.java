package org.jaudiotagger.audio.mp4.atom;

import java.nio.ByteBuffer;

import org.jaudiotagger.audio.exceptions.CannotReadException;

/**
 * DrmsBox Replaces mp4a box on drm files
 * <p/>
 * Need to skip over data in order to find esds atom
 * <p/>
 * Specification not known, so just look for byte by byte 'esds' and then step back four bytes for size
 */
public class Mp4DrmsBox extends AbstractMp4Box {
	/**
	 * @param header
	 *            header info
	 * @param dataBuffer
	 *            data of box (doesnt include header data)
	 */
	public Mp4DrmsBox(final Mp4BoxHeader header, final ByteBuffer dataBuffer) {
		this.header = header;
		this.dataBuffer = dataBuffer;
	}

	/**
	 * Process direct data
	 * 
	 * @throws CannotReadException
	 */
	public void processData() throws CannotReadException {
		while (dataBuffer.hasRemaining()) {
			final byte next = dataBuffer.get();
			if (next != (byte) 'e')
				continue;

			// Have we found esds identifier, if so adjust buffer to start of esds atom
			final ByteBuffer tempBuffer = dataBuffer.slice();
			if ((tempBuffer.get() == (byte) 's') & (tempBuffer.get() == (byte) 'd') & (tempBuffer.get() == (byte) 's')) {
				dataBuffer.position(dataBuffer.position() - 1 - Mp4BoxHeader.OFFSET_LENGTH);
				return;
			}
		}
	}
}
