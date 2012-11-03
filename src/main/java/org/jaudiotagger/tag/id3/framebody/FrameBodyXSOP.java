package org.jaudiotagger.tag.id3.framebody;

import java.nio.ByteBuffer;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

/**
 * Artist Sort name, this is what MusicBrainz uses in ID3v23 because TSOP not supported.
 * <p/>
 * However iTunes uses TSOP even in ID3v23, so we have two possible options
 */
public class FrameBodyXSOP extends AbstractFrameBodyTextInfo implements ID3v23FrameBody {
	/**
	 * Creates a new FrameBodyTSOT datatype.
	 */
	public FrameBodyXSOP() {}

	public FrameBodyXSOP(final FrameBodyXSOP body) {
		super(body);
	}

	/**
	 * Creates a new FrameBodyTSOT datatype.
	 * 
	 * @param textEncoding
	 * @param text
	 */
	public FrameBodyXSOP(final byte textEncoding, final String text) {
		super(textEncoding, text);
	}

	/**
	 * Creates a new FrameBodyTSOT datatype.
	 * 
	 * @param byteBuffer
	 * @param frameSize
	 * @throws InvalidTagException
	 */
	public FrameBodyXSOP(final ByteBuffer byteBuffer, final int frameSize) throws InvalidTagException {
		super(byteBuffer, frameSize);
	}

	/**
	 * The ID3v2 frame identifier
	 * 
	 * @return the ID3v2 frame identifier for this frame type
	 */
	@Override
	public String getIdentifier() {
		return ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ;
	}
}
