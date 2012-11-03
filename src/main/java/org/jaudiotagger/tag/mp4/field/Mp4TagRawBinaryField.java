package org.jaudiotagger.tag.mp4.field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.Mp4TagField;

/**
 * Represents raw binary data
 * <p/>
 * <p>
 * We use this when we find an atom under the ilst atom that we do not recognise , that does not follow standard
 * conventions in order to save the data without modification so it can be safetly written back to file
 */
public class Mp4TagRawBinaryField extends Mp4TagField {
	protected int dataSize;
	protected byte[] dataBytes;

	/**
	 * Construct binary field from rawdata of audio file
	 * 
	 * @param header
	 * @param raw
	 * @throws java.io.UnsupportedEncodingException
	 * 
	 */
	public Mp4TagRawBinaryField(final Mp4BoxHeader header, final ByteBuffer raw) throws UnsupportedEncodingException {
		super(header.getId());
		dataSize = header.getDataLength();
		build(raw);
	}

	@Override
	public Mp4FieldType getFieldType() {
		return Mp4FieldType.IMPLICIT;
	}

	/**
	 * Used when creating raw content
	 * 
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * 
	 */
	@Override
	protected byte[] getDataBytes() throws UnsupportedEncodingException {
		return dataBytes;
	}

	/**
	 * Build from data
	 * <p/>
	 * <p>
	 * After returning buffers position will be after the end of this atom
	 * 
	 * @param raw
	 */
	@Override
	protected void build(final ByteBuffer raw) {
		// Read the raw data into byte array
		this.dataBytes = new byte[dataSize];
		for (int i = 0; i < dataBytes.length; i++)
			this.dataBytes[i] = raw.get();
	}

	@Override
	public boolean isBinary() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.dataBytes.length == 0;
	}

	public int getDataSize() {
		return dataSize;

	}

	public byte[] getData() {
		return this.dataBytes;
	}

	public void setData(final byte[] d) {
		this.dataBytes = d;
	}

	@Override
	public void copyContent(final TagField field) {
		throw new UnsupportedOperationException("not done");
	}

	@Override
	public byte[] getRawContent() throws UnsupportedEncodingException {
		logger.fine("Getting Raw data for:" + getId());
		try {
			final ByteArrayOutputStream outerbaos = new ByteArrayOutputStream();
			outerbaos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + dataSize));
			outerbaos.write(Utils.getDefaultBytes(getId(), "ISO-8859-1"));
			outerbaos.write(dataBytes);
			System.out.println("SIZE" + outerbaos.size());
			return outerbaos.toByteArray();
		} catch (final IOException ioe) {
			// This should never happen as were not actually writing to/from a file
			throw new RuntimeException(ioe);
		}
	}

}
