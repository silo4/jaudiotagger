package org.jaudiotagger.tag.id3;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.InvalidFrameException;

/**
 * compresses frame data
 * <p/>
 * Is currently required for V23Frames and V24Frames
 * <p/>
 */
// TODO also need to support compress framedata
public class ID3Compression {
	// Logger
	public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.id3");

	/**
	 * Decompress realFrameSize bytes to decompressedFrameSize bytes and return as ByteBuffer
	 * 
	 * @param byteBuffer
	 * @param decompressedFrameSize
	 * @param realFrameSize
	 * @return
	 * @throws org.jaudiotagger.tag.InvalidFrameException
	 * 
	 */
	protected static ByteBuffer uncompress(final String identifier, final String filename, final ByteBuffer byteBuffer, final int decompressedFrameSize, final int realFrameSize) throws InvalidFrameException {
		logger.config(filename + ":About to decompress " + realFrameSize + " bytes, expect result to be:" + decompressedFrameSize + " bytes");
		// Decompress the bytes into this buffer, size initialized from header field
		final byte[] result = new byte[decompressedFrameSize];
		final byte[] input = new byte[realFrameSize];

		// Store position ( just after frame header and any extra bits)
		// Read frame data into array, and then put buffer back to where it was
		final int position = byteBuffer.position();
		byteBuffer.get(input, 0, realFrameSize);
		byteBuffer.position(position);

		final Inflater decompresser = new Inflater();
		decompresser.setInput(input);
		try {
			final int inflatedTo = decompresser.inflate(result);
			logger.config(filename + ":Decompressed to " + inflatedTo + " bytes");
		} catch (final DataFormatException dfe) {
			logger.log(Level.CONFIG, "Unable to decompress this frame:" + identifier, dfe);

			// Update position of main buffer, so no attempt is made to reread these bytes
			byteBuffer.position(byteBuffer.position() + realFrameSize);
			throw new InvalidFrameException(ErrorMessage.ID3_UNABLE_TO_DECOMPRESS_FRAME.getMsg(identifier, filename, dfe.getMessage()));
		}
		decompresser.end();
		return ByteBuffer.wrap(result);
	}

}
