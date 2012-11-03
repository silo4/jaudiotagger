package org.jaudiotagger.tag.mp4.field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4TagField;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import org.jaudiotagger.tag.mp4.atom.Mp4MeanBox;
import org.jaudiotagger.tag.mp4.atom.Mp4NameBox;

/**
 * Represents reverse dns field, used for custom information
 * <p/>
 * <p>
 * Originally only used by Itunes for information that was iTunes specific but now used in a wide range of uses, for
 * example Musicbrainz uses it for many of its fields.
 * <p/>
 * These fields have a more complex setup Box ---- shows this is a reverse dns metadata field Box mean the issuer in the
 * form of reverse DNS domain (e.g com.apple.iTunes) Box name descriptor identifying the type of contents Box data
 * contents
 * <p/>
 * The raw data passed starts from the mean box
 */
public class Mp4TagReverseDnsField extends Mp4TagField implements TagTextField {
	public static final String IDENTIFIER = "----";

	protected int dataSize;

	// Issuer
	private String issuer;

	// Descriptor
	private String descriptor;

	// Data Content,
	// TODO assuming always text at the moment
	protected String content;

	/**
	 * Construct from existing file data
	 * 
	 * @param parentHeader
	 * @param data
	 * @throws UnsupportedEncodingException
	 */
	public Mp4TagReverseDnsField(final Mp4BoxHeader parentHeader, final ByteBuffer data) throws UnsupportedEncodingException {
		super(parentHeader, data);
	}

	/**
	 * Newly created Reverse Dns field
	 * 
	 * @param id
	 * @param content
	 */
	public Mp4TagReverseDnsField(final Mp4FieldKey id, final String content) {
		super(id.getFieldName());
		this.issuer = id.getIssuer();
		this.descriptor = id.getIdentifier();
		this.content = content;
	}

	/**
	 * Newly created Reverse Dns field bypassing the Mp4TagField enum for creation of temporary reverse dns fields
	 * 
	 * @param fieldName
	 * @param issuer
	 * @param identifier
	 * @param content
	 */
	public Mp4TagReverseDnsField(final String fieldName, final String issuer, final String identifier, final String content) {
		super(fieldName);
		this.issuer = issuer;
		this.descriptor = identifier;
		this.content = content;
	}

	@Override
	public Mp4FieldType getFieldType() {
		// TODO always assuming text at moment but may not always be the case (though dont have any concrete
		// examples)
		return Mp4FieldType.TEXT;
	}

	@Override
	protected void build(final ByteBuffer data) throws UnsupportedEncodingException {
		// Read mean box, set the issuer and skip over data
		final Mp4BoxHeader meanBoxHeader = new Mp4BoxHeader(data);
		final Mp4MeanBox meanBox = new Mp4MeanBox(meanBoxHeader, data);
		setIssuer(meanBox.getIssuer());
		data.position(data.position() + meanBoxHeader.getDataLength());

		// Read name box, identify what type of field it is
		final Mp4BoxHeader nameBoxHeader = new Mp4BoxHeader(data);
		final Mp4NameBox nameBox = new Mp4NameBox(nameBoxHeader, data);
		setDescriptor(nameBox.getName());
		data.position(data.position() + nameBoxHeader.getDataLength());

		// Issue 198:There is not actually a data atom there cannot cant be because no room for one
		if (parentHeader.getDataLength() == meanBoxHeader.getLength() + nameBoxHeader.getLength()) {
			id = IDENTIFIER + ":" + issuer + ":" + descriptor;
			setContent("");
			logger.warning(ErrorMessage.MP4_REVERSE_DNS_FIELD_HAS_NO_DATA.getMsg(id));
		}
		// Usual Case
		else {
			// Read data box, identify the data
			final Mp4BoxHeader dataBoxHeader = new Mp4BoxHeader(data);
			final Mp4DataBox dataBox = new Mp4DataBox(dataBoxHeader, data);
			setContent(dataBox.getContent());
			data.position(data.position() + dataBoxHeader.getDataLength());

			// Now calculate the id which in order to be unique needs to use all htree values
			id = IDENTIFIER + ":" + issuer + ":" + descriptor;
		}
	}

	@Override
	public void copyContent(final TagField field) {
		if (field instanceof Mp4TagReverseDnsField) {
			this.issuer = ((Mp4TagReverseDnsField) field).getIssuer();
			this.descriptor = ((Mp4TagReverseDnsField) field).getDescriptor();
			this.content = ((Mp4TagReverseDnsField) field).getContent();
		}
	}

	/**
	 * @return content
	 */
	@Override
	public String getContent() {
		return content;
	}

	@Override
	protected byte[] getDataBytes() throws UnsupportedEncodingException {
		return content.getBytes(getEncoding());
	}

	@Override
	public String getEncoding() {
		return Mp4BoxHeader.CHARSET_UTF_8;
	}

	/**
	 * Convert back to raw content, includes ----,mean,name and data atom as views as one thing externally
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public byte[] getRawContent() throws UnsupportedEncodingException {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// Create Meanbox data
			final byte[] issuerRawData = issuer.getBytes(getEncoding());
			baos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + Mp4MeanBox.PRE_DATA_LENGTH + issuerRawData.length));
			baos.write(Utils.getDefaultBytes(Mp4MeanBox.IDENTIFIER, "ISO-8859-1"));
			baos.write(new byte[] { 0, 0, 0, 0 });
			baos.write(issuerRawData);

			// Create Namebox data
			final byte[] nameRawData = descriptor.getBytes(getEncoding());
			baos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + Mp4NameBox.PRE_DATA_LENGTH + nameRawData.length));
			baos.write(Utils.getDefaultBytes(Mp4NameBox.IDENTIFIER, "ISO-8859-1"));
			baos.write(new byte[] { 0, 0, 0, 0 });
			baos.write(nameRawData);

			// Create DataBox data if we have data only
			if (content.length() > 0)
				baos.write(getRawContentDataOnly());
			// Now wrap with reversedns box
			final ByteArrayOutputStream outerbaos = new ByteArrayOutputStream();
			outerbaos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + baos.size()));
			outerbaos.write(Utils.getDefaultBytes(IDENTIFIER, "ISO-8859-1"));
			outerbaos.write(baos.toByteArray());
			return outerbaos.toByteArray();

		} catch (final IOException ioe) {
			// This should never happen as were not actually writing to/from a file
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public byte[] getRawContentDataOnly() throws UnsupportedEncodingException {
		logger.fine("Getting Raw data for:" + getId());
		try {
			// Create DataBox data
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final byte[] dataRawData = content.getBytes(getEncoding());
			baos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + Mp4DataBox.PRE_DATA_LENGTH + dataRawData.length));
			baos.write(Utils.getDefaultBytes(Mp4DataBox.IDENTIFIER, "ISO-8859-1"));
			baos.write(new byte[] { 0 });
			baos.write(new byte[] { 0, 0, (byte) getFieldType().getFileClassId() });
			baos.write(new byte[] { 0, 0, 0, 0 });
			baos.write(dataRawData);
			return baos.toByteArray();
		} catch (final IOException ioe) {
			// This should never happen as were not actually writing to/from a file
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return this.content.trim().equals("");
	}

	@Override
	public void setContent(final String s) {
		this.content = s;
	}

	@Override
	public void setEncoding(final String s) {
		/* Not allowed */
	}

	@Override
	public String toString() {
		return content;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * Set the issuer, usually reverse dns of the Companies domain
	 * 
	 * @param issuer
	 */
	public void setIssuer(final String issuer) {
		this.issuer = issuer;
	}

	/**
	 * Set the descriptor for the data (what type of data it is)
	 * 
	 * @param descriptor
	 */
	public void setDescriptor(final String descriptor) {
		this.descriptor = descriptor;
	}
}
