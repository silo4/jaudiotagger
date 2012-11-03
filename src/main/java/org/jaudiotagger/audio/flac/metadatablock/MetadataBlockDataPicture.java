package org.jaudiotagger.audio.flac.metadatablock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.InvalidFrameException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.PictureTypes;

/**
 * Picture Block
 * <p/>
 * <p/>
 * <p>
 * This block is for storing pictures associated with the file, most commonly cover art from CDs. There may be more than
 * one PICTURE block in a file. The picture format is similar to the APIC frame in ID3v2. The PICTURE block has a type,
 * MIME type, and UTF-8 description like ID3v2, and supports external linking via URL (though this is discouraged). The
 * differences are that there is no uniqueness constraint on the description field, and the MIME type is mandatory. The
 * FLAC PICTURE block also includes the resolution, color depth, and palette size so that the client can search for a
 * suitable picture without having to scan them all
 * <p/>
 * Format: <Size in bits> Info <32> The picture type according to the ID3v2 APIC frame: (There may only be one each of
 * picture type 1 and 2 in a file) <32> The length of the MIME type string in bytes. <n*8> The MIME type string, in
 * printable ASCII characters 0x20-0x7e. The MIME type may also be --> to signify that the data part is a URL of the
 * picture instead of the picture data itself. <32> The length of the description string in bytes. <n*8> The description
 * of the picture, in UTF-8. <32> The width of the picture in pixels. <32> The height of the picture in pixels. <32> The
 * color depth of the picture in bits-per-pixel. <32> For indexed-color pictures (e.g. GIF), the number of colors used,
 * or 0 for non-indexed pictures. <32> The length of the picture data in bytes. <n*8> The binary picture data.
 */
public class MetadataBlockDataPicture implements MetadataBlockData, TagField {
	public static final String IMAGE_IS_URL = "-->";

	private int pictureType;
	private String mimeType;
	private String description;
	private int width;
	private int height;
	private int colourDepth;
	private int indexedColouredCount;
	private byte[] imageData;

	// Logger Object
	public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.flac.MetadataBlockDataPicture");

	private void initFromByteBuffer(final ByteBuffer rawdata) throws IOException, InvalidFrameException {
		// Picture Type
		pictureType = rawdata.getInt();
		if (pictureType >= PictureTypes.getInstanceOf().getSize())
			throw new InvalidFrameException("PictureType was:" + pictureType + "but the maximum allowed is " + (PictureTypes.getInstanceOf().getSize() - 1));

		// MimeType
		final int mimeTypeSize = rawdata.getInt();
		mimeType = getString(rawdata, mimeTypeSize, "ISO-8859-1");

		// Description
		final int descriptionSize = rawdata.getInt();
		description = getString(rawdata, descriptionSize, "UTF-8");

		// Image width
		width = rawdata.getInt();

		// Image height
		height = rawdata.getInt();

		// Colour Depth
		colourDepth = rawdata.getInt();

		// Indexed Colour Count
		indexedColouredCount = rawdata.getInt();

		// ImageData
		final int rawdataSize = rawdata.getInt();
		imageData = new byte[rawdataSize];
		rawdata.get(imageData);

		logger.config("Read image:" + this.toString());
	}

	/**
	 * Initialize MetaBlockDataPicture from byteBuffer
	 * 
	 * @param rawdata
	 * @throws IOException
	 * @throws InvalidFrameException
	 */
	public MetadataBlockDataPicture(final ByteBuffer rawdata) throws IOException, InvalidFrameException {
		initFromByteBuffer(rawdata);
	}

	/**
	 * Construct picture block by reading from file, the header informs us how many bytes we should be reading from
	 * 
	 * @param header
	 * @param raf
	 * @throws java.io.IOException
	 * @throws org.jaudiotagger.tag.InvalidFrameException
	 */
	// TODO check for buffer underflows see http://research.eeye.com/html/advisories/published/AD20071115.html
	public MetadataBlockDataPicture(final MetadataBlockHeader header, final RandomAccessFile raf) throws IOException, InvalidFrameException {
		final ByteBuffer rawdata = ByteBuffer.allocate(header.getDataLength());
		final int bytesRead = raf.getChannel().read(rawdata);
		if (bytesRead < header.getDataLength())
			throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
		rawdata.rewind();
		initFromByteBuffer(rawdata);

	}

	/**
	 * Construct new MetadataPicture block
	 * 
	 * @param imageData
	 * @param pictureType
	 * @param mimeType
	 * @param description
	 * @param width
	 * @param height
	 * @param colourDepth
	 * @param indexedColouredCount
	 */
	public MetadataBlockDataPicture(final byte[] imageData, final int pictureType, final String mimeType, final String description, final int width, final int height, final int colourDepth, final int indexedColouredCount) {
		// Picture Type
		this.pictureType = pictureType;

		// MimeType
		this.mimeType = mimeType;

		// Description
		this.description = description;

		this.width = width;

		this.height = height;

		this.colourDepth = colourDepth;

		this.indexedColouredCount = indexedColouredCount;
		// ImageData
		this.imageData = imageData;
	}

	private String getString(final ByteBuffer rawdata, final int length, final String charset) throws IOException {
		final byte[] tempbuffer = new byte[length];
		rawdata.get(tempbuffer);
		return new String(tempbuffer, charset);
	}

	@Override
	public byte[] getBytes() {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(Utils.getSizeBEInt32(pictureType));
			baos.write(Utils.getSizeBEInt32(mimeType.length()));
			baos.write(mimeType.getBytes("ISO-8859-1"));
			baos.write(Utils.getSizeBEInt32(description.length()));
			baos.write(description.getBytes("UTF-8"));
			baos.write(Utils.getSizeBEInt32(width));
			baos.write(Utils.getSizeBEInt32(height));
			baos.write(Utils.getSizeBEInt32(colourDepth));
			baos.write(Utils.getSizeBEInt32(indexedColouredCount));
			baos.write(Utils.getSizeBEInt32(imageData.length));
			baos.write(imageData);
			return baos.toByteArray();

		} catch (final IOException ioe) {
			throw new RuntimeException(ioe.getMessage());
		}
	}

	@Override
	public int getLength() {
		return getBytes().length;
	}

	public int getPictureType() {
		return pictureType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getDescription() {
		return description;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getColourDepth() {
		return colourDepth;
	}

	public int getIndexedColourCount() {
		return indexedColouredCount;
	}

	public byte[] getImageData() {
		return imageData;
	}

	/**
	 * @return true if imagedata is held as a url rather than actually being imagedata
	 */
	public boolean isImageUrl() {
		return getMimeType().equals(IMAGE_IS_URL);
	}

	/**
	 * @return the image url if there is otherwise return an empty String
	 */
	public String getImageUrl() {
		if (isImageUrl())
			return Utils.getString(getImageData(), 0, getImageData().length, TextEncoding.CHARSET_ISO_8859_1);
		else
			return "";
	}

	@Override
	public String toString() {
		return PictureTypes.getInstanceOf().getValueForId(pictureType) + ":" + mimeType + ":" + description + ":" + "width:" + width + ":height:" + height + ":colourdepth:" + colourDepth + ":indexedColourCount:" + indexedColouredCount
				+ ":image size in bytes:" + imageData.length;
	}

	/**
	 * This method copies the data of the given field to the current data.<br>
	 * 
	 * @param field
	 *            The field containing the data to be taken.
	 */
	@Override
	public void copyContent(final TagField field) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the Id of the represented tag field.<br>
	 * This value should uniquely identify a kind of tag data, like title.
	 * {@link org.jaudiotagger.audio.generic.AbstractTag} will use the &quot;id&quot; to summarize multiple fields.
	 * 
	 * @return Unique identifier for the fields type. (title, artist...)
	 */
	@Override
	public String getId() {
		return FieldKey.COVER_ART.name();
	}

	/**
	 * This method delivers the binary representation of the fields data in order to be directly written to the file.<br>
	 * 
	 * @return Binary data representing the current tag field.<br>
	 * @throws java.io.UnsupportedEncodingException
	 *             Most tag data represents text. In some cases the underlying implementation will need to convert the
	 *             text data in java to a specific charset encoding. In these cases an
	 *             {@link java.io.UnsupportedEncodingException} may occur.
	 */
	@Override
	public byte[] getRawContent() throws UnsupportedEncodingException {
		return getBytes();
	}

	/**
	 * Determines whether the represented field contains (is made up of) binary data, instead of text data.<br>
	 * Software can identify fields to be displayed because they are human readable if this method returns
	 * <code>false</code>.
	 * 
	 * @return <code>true</code> if field represents binary data (not human readable).
	 */
	@Override
	public boolean isBinary() {
		return true;
	}

	/**
	 * This method will set the field to represent binary data.<br>
	 * <p/>
	 * Some implementations may support conversions.<br>
	 * As of now (Octobre 2005) there is no implementation really using this method to perform useful operations.
	 * 
	 * @param b
	 *            <code>true</code>, if the field contains binary data.
	 * @deprecated As for now is of no use. Implementations should use another way of setting this property.
	 */
	@Override
	@Deprecated
	public void isBinary(final boolean b) {
		// Do nothing, always true
	}

	/**
	 * Identifies a field to be of common use.<br>
	 * <p/>
	 * Some software may differ between common and not common fields. A common one is for sure the title field. A web
	 * link may not be of common use for tagging. However some file formats, or future development of users expectations
	 * will make more fields common than now can be known.
	 * 
	 * @return <code>true</code> if the field is of common use.
	 */
	@Override
	public boolean isCommon() {
		return true;
	}

	/**
	 * Determines whether the content of the field is empty.<br>
	 * 
	 * @return <code>true</code> if no data is stored (or empty String).
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

}
