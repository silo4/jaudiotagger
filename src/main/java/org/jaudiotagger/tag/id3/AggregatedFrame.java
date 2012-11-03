package org.jaudiotagger.tag.id3;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Required when a single generic field maps to multiple ID3 Frames
 */
public class AggregatedFrame implements TagTextField {
	// TODO rather than just maintaining insertion order we want to define a preset order
	protected Set<AbstractID3v2Frame> frames = new LinkedHashSet<AbstractID3v2Frame>();

	public void addFrame(final AbstractID3v2Frame frame) {
		frames.add(frame);
	}

	public Set<AbstractID3v2Frame> getFrames() {
		return frames;
	}

	/**
	 * Returns the content of the underlying frames in order.
	 * 
	 * @return Content
	 */
	@Override
	public String getContent() {
		final StringBuilder sb = new StringBuilder();
		for (final AbstractID3v2Frame next : frames)
			sb.append(next.getContent());
		return sb.toString();
	}

	/**
	 * Returns the current used charset encoding.
	 * 
	 * @return Charset encoding.
	 */
	@Override
	public String getEncoding() {
		return TextEncoding.getInstanceOf().getValueForId(frames.iterator().next().getBody().getTextEncoding());
	}

	/**
	 * Sets the content of the field.
	 * 
	 * @param content
	 *            fields content.
	 */
	@Override
	public void setContent(final String content) {

	}

	/**
	 * Sets the charset encoding used by the field.
	 * 
	 * @param encoding
	 *            charset.
	 */
	@Override
	public void setEncoding(final String encoding) {

	}

	// TODO:needs implementing but not sure if this method is required at all
	@Override
	public void copyContent(final TagField field) {

	}

	@Override
	public String getId() {
		final StringBuilder sb = new StringBuilder();
		for (final AbstractID3v2Frame next : frames)
			sb.append(next.getId());
		return sb.toString();
	}

	@Override
	public boolean isCommon() {
		return true;
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public void isBinary(final boolean b) {
		;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public byte[] getRawContent() throws UnsupportedEncodingException {
		throw new UnsupportedEncodingException();
	}
}
