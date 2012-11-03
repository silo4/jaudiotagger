package org.jaudiotagger.tag.flac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.reference.PictureTypes;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

/**
 * Flac uses Vorbis Comment for most of its metadata and a Flac Picture Block for images
 * <p/>
 * <p/>
 * This class enscapulates the items into a single tag
 */
public class FlacTag implements Tag {
	private VorbisCommentTag tag = null;
	private List<MetadataBlockDataPicture> images = new ArrayList<MetadataBlockDataPicture>();

	public FlacTag() {
		this(VorbisCommentTag.createNewTag(), new ArrayList<MetadataBlockDataPicture>());
	}

	public FlacTag(final VorbisCommentTag tag, final List<MetadataBlockDataPicture> images) {
		this.tag = tag;
		this.images = images;
	}

	/**
	 * @return images
	 */
	public List<MetadataBlockDataPicture> getImages() {
		return images;
	}

	/**
	 * @return the vorbis tag (this is what handles text metadata)
	 */
	public VorbisCommentTag getVorbisCommentTag() {
		return tag;
	}

	@Override
	public void addField(final TagField field) throws FieldDataInvalidException {
		if (field instanceof MetadataBlockDataPicture)
			images.add((MetadataBlockDataPicture) field);
		else
			tag.addField(field);
	}

	@Override
	public List<TagField> getFields(final String id) {
		if (id.equals(FieldKey.COVER_ART.name())) {
			final List<TagField> castImages = new ArrayList<TagField>();
			for (final MetadataBlockDataPicture image : images)
				castImages.add(image);
			return castImages;
		} else
			return tag.getFields(id);
	}

	/**
	 * Maps the generic key to the specific key and return the list of values for this field as strings
	 * 
	 * @param genericKey
	 * @return
	 * @throws KeyNotFoundException
	 */
	@Override
	public List<String> getAll(final FieldKey genericKey) throws KeyNotFoundException {
		if (genericKey == FieldKey.COVER_ART)
			throw new UnsupportedOperationException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD.getMsg());
		else
			return tag.getAll(genericKey);
	}

	@Override
	public boolean hasCommonFields() {
		return tag.hasCommonFields();
	}

	/**
	 * Determines whether the tag has no fields specified.<br>
	 * <p/>
	 * <p>
	 * If there are no images we return empty if either there is no VorbisTag or if there is a VorbisTag but it is empty
	 * 
	 * @return <code>true</code> if tag contains no field.
	 */
	@Override
	public boolean isEmpty() {
		return (tag == null || tag.isEmpty()) && images.size() == 0;
	}

	@Override
	public void setField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		final TagField tagfield = createField(genericKey, value);
		setField(tagfield);
	}

	@Override
	public void addField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		final TagField tagfield = createField(genericKey, value);
		addField(tagfield);
	}

	/**
	 * Create and set field with name of vorbisCommentkey
	 * 
	 * @param vorbisCommentKey
	 * @param value
	 * @throws KeyNotFoundException
	 * @throws FieldDataInvalidException
	 */
	public void setField(final String vorbisCommentKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		final TagField tagfield = createField(vorbisCommentKey, value);
		setField(tagfield);
	}

	/**
	 * Create and add field with name of vorbisCommentkey
	 * 
	 * @param vorbisCommentKey
	 * @param value
	 * @throws KeyNotFoundException
	 * @throws FieldDataInvalidException
	 */
	public void addField(final String vorbisCommentKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		final TagField tagfield = createField(vorbisCommentKey, value);
		addField(tagfield);
	}

	/**
	 * @param field
	 * @throws FieldDataInvalidException
	 */
	@Override
	public void setField(final TagField field) throws FieldDataInvalidException {
		if (field instanceof MetadataBlockDataPicture) {
			if (images.size() == 0)
				images.add(0, (MetadataBlockDataPicture) field);
			else
				images.set(0, (MetadataBlockDataPicture) field);
		} else
			tag.setField(field);
	}

	@Override
	public TagField createField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		if (genericKey.equals(FieldKey.COVER_ART))
			throw new UnsupportedOperationException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD.getMsg());
		else
			return tag.createField(genericKey, value);
	}

	/**
	 * Create Tag Field using ogg key
	 * 
	 * @param vorbisCommentFieldKey
	 * @param value
	 * @return
	 * @throws org.jaudiotagger.tag.KeyNotFoundException
	 * @throws org.jaudiotagger.tag.FieldDataInvalidException
	 */
	public TagField createField(final VorbisCommentFieldKey vorbisCommentFieldKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		if (vorbisCommentFieldKey.equals(VorbisCommentFieldKey.COVERART))
			throw new UnsupportedOperationException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD.getMsg());
		return tag.createField(vorbisCommentFieldKey, value);
	}

	/**
	 * Create Tag Field using ogg key
	 * <p/>
	 * This method is provided to allow you to create key of any value because VorbisComment allows arbitary keys.
	 * 
	 * @param vorbisCommentFieldKey
	 * @param value
	 * @return
	 */
	public TagField createField(final String vorbisCommentFieldKey, final String value) {
		if (vorbisCommentFieldKey.equals(VorbisCommentFieldKey.COVERART.getFieldName()))
			throw new UnsupportedOperationException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD.getMsg());
		return tag.createField(vorbisCommentFieldKey, value);
	}

	@Override
	public String getFirst(final String id) {
		if (id.equals(FieldKey.COVER_ART.name()))
			throw new UnsupportedOperationException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD.getMsg());
		else
			return tag.getFirst(id);
	}

	@Override
	public String getValue(final FieldKey id, final int index) throws KeyNotFoundException {
		if (id.equals(FieldKey.COVER_ART))
			throw new UnsupportedOperationException(ErrorMessage.ARTWORK_CANNOT_BE_RETRIEVED_WITH_THIS_METHOD.getMsg());
		else
			return tag.getValue(id, index);
	}

	@Override
	public String getFirst(final FieldKey id) throws KeyNotFoundException {
		return getValue(id, 0);
	}

	@Override
	public TagField getFirstField(final String id) {
		if (id.equals(FieldKey.COVER_ART.name())) {
			if (images.size() > 0)
				return images.get(0);
			else
				return null;
		} else
			return tag.getFirstField(id);
	}

	@Override
	public TagField getFirstField(final FieldKey genericKey) throws KeyNotFoundException {
		if (genericKey == null)
			throw new KeyNotFoundException();

		if (genericKey == FieldKey.COVER_ART)
			return getFirstField(FieldKey.COVER_ART.name());
		else
			return tag.getFirstField(genericKey);
	}

	/**
	 * Delete any instance of tag fields with this key
	 * 
	 * @param fieldKey
	 */
	@Override
	public void deleteField(final FieldKey fieldKey) throws KeyNotFoundException {
		if (fieldKey.equals(FieldKey.COVER_ART))
			images.clear();
		else
			tag.deleteField(fieldKey);
	}

	@Override
	public void deleteField(final String id) throws KeyNotFoundException {
		if (id.equals(FieldKey.COVER_ART.name()))
			images.clear();
		else
			tag.deleteField(id);
	}

	// TODO addField images to iterator
	@Override
	public Iterator<TagField> getFields() {
		return tag.getFields();
	}

	@Override
	public int getFieldCount() {
		return tag.getFieldCount() + images.size();
	}

	@Override
	public int getFieldCountIncludingSubValues() {
		return getFieldCount();
	}

	@Override
	public boolean setEncoding(final String enc) throws FieldDataInvalidException {
		return tag.setEncoding(enc);
	}

	@Override
	public List<TagField> getFields(final FieldKey id) throws KeyNotFoundException {
		if (id.equals(FieldKey.COVER_ART)) {
			final List<TagField> castImages = new ArrayList<TagField>();
			for (final MetadataBlockDataPicture image : images)
				castImages.add(image);
			return castImages;
		} else
			return tag.getFields(id);
	}

	public TagField createArtworkField(final byte[] imageData, final int pictureType, final String mimeType, final String description, final int width, final int height, final int colourDepth, final int indexedColouredCount)
			throws FieldDataInvalidException {
		return new MetadataBlockDataPicture(imageData, pictureType, mimeType, description, width, height, colourDepth, indexedColouredCount);
	}

	/**
	 * Create Link to Image File, not recommended because if either flac or image file is moved link will be broken.
	 * 
	 * @param url
	 * @return
	 */
	public TagField createLinkedArtworkField(final String url) {
		// Add to image list
		return new MetadataBlockDataPicture(Utils.getDefaultBytes(url, TextEncoding.CHARSET_ISO_8859_1), PictureTypes.DEFAULT_ID, MetadataBlockDataPicture.IMAGE_IS_URL, "", 0, 0, 0, 0);
	}

	/**
	 * Create artwork field
	 * 
	 * @return
	 */
	@Override
	public TagField createField(final Artwork artwork) throws FieldDataInvalidException {
		if (artwork.isLinked())
			return new MetadataBlockDataPicture(Utils.getDefaultBytes(artwork.getImageUrl(), TextEncoding.CHARSET_ISO_8859_1), artwork.getPictureType(), MetadataBlockDataPicture.IMAGE_IS_URL, "", 0, 0, 0, 0);
		else {
			if (!artwork.setImageFromData())
				throw new FieldDataInvalidException("Unable to createField buffered image from the image");

			return new MetadataBlockDataPicture(artwork.getBinaryData(), artwork.getPictureType(), artwork.getMimeType(), artwork.getDescription(), artwork.getWidth(), artwork.getHeight(), 0, 0);
		}
	}

	@Override
	public void setField(final Artwork artwork) throws FieldDataInvalidException {
		this.setField(createField(artwork));
	}

	@Override
	public void addField(final Artwork artwork) throws FieldDataInvalidException {
		this.addField(createField(artwork));
	}

	@Override
	public List<Artwork> getArtworkList() {
		final List<Artwork> artworkList = new ArrayList<Artwork>(images.size());

		for (final MetadataBlockDataPicture coverArt : images) {
			final Artwork artwork = ArtworkFactory.createArtworkFromMetadataBlockDataPicture(coverArt);
			artworkList.add(artwork);
		}
		return artworkList;
	}

	@Override
	public Artwork getFirstArtwork() {
		final List<Artwork> artwork = getArtworkList();
		if (artwork.size() > 0)
			return artwork.get(0);
		return null;
	}

	/**
	 * Delete all instance of artwork Field
	 * 
	 * @throws KeyNotFoundException
	 */
	@Override
	public void deleteArtworkField() throws KeyNotFoundException {
		this.deleteField(FieldKey.COVER_ART);
	}

	/**
	 * 
	 * @param genericKey
	 * @return
	 */
	@Override
	public boolean hasField(final FieldKey genericKey) {
		if (genericKey == FieldKey.COVER_ART)
			return images.size() > 0;
		else
			return tag.hasField(genericKey);
	}

	/**
	 * 
	 * @param vorbisFieldKey
	 * @return
	 */
	public boolean hasField(final VorbisCommentFieldKey vorbisFieldKey) {
		return tag.hasField(vorbisFieldKey);
	}

	@Override
	public boolean hasField(final String id) {
		if (id.equals(FieldKey.COVER_ART.name()))
			return images.size() > 0;
		else
			return tag.hasField(id);
	}

}
