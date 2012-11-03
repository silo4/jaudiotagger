package org.jaudiotagger.tag.id3.framebody;

import java.nio.ByteBuffer;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

/**
 * Composer Sort name (iTunes Only)
 */
public class FrameBodyTSOC extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody {
	/**
	 * Creates a new FrameBodyTSOC datatype.
	 */
	public FrameBodyTSOC() {}

	public FrameBodyTSOC(final FrameBodyTSOC body) {
		super(body);
	}

	/**
	 * Creates a new FrameBodyTSOA datatype.
	 * 
	 * @param textEncoding
	 * @param text
	 */
	public FrameBodyTSOC(final byte textEncoding, final String text) {
		super(textEncoding, text);
	}

	/**
	 * Creates a new FrameBodyTSOA datatype.
	 * 
	 * @param byteBuffer
	 * @param frameSize
	 * @throws InvalidTagException
	 */
	public FrameBodyTSOC(final ByteBuffer byteBuffer, final int frameSize) throws InvalidTagException {
		super(byteBuffer, frameSize);
	}

	/**
	 * The ID3v2 frame identifier
	 * 
	 * @return the ID3v2 frame identifier for this frame type
	 */
	@Override
	public String getIdentifier() {
		return ID3v24Frames.FRAME_ID_COMPOSER_SORT_ORDER_ITUNES;
	}
}
