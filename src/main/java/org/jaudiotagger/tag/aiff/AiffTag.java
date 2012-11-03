package org.jaudiotagger.tag.aiff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.images.Artwork;

/** AiffTag wraps ID3Tag for most of its metadata */
public class AiffTag /* extends GenericTag */implements Tag {

	private AbstractID3v2Tag id3Tag;

	/** No-argument constructor */
	public AiffTag() {}

	public AiffTag(final AbstractID3v2Tag t) {
		id3Tag = t;
	}

	/** Returns the ID3 tag */
	public AbstractID3v2Tag getID3Tag() {
		return id3Tag;
	}

	/** Sets the ID3 tag */
	public void setID3Tag(final AbstractID3v2Tag t) {
		id3Tag = t;
	}

	@Override
	public void addField(final TagField field) throws FieldDataInvalidException {
		id3Tag.addField(field);
	}

	@Override
	public List<TagField> getFields(final String id) {
		return id3Tag.getFields(id);
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
		return id3Tag.getAll(genericKey);
	}

	@Override
	public boolean hasCommonFields() {
		return id3Tag.hasCommonFields();
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
		return (id3Tag == null || id3Tag.isEmpty());
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
	 * @param field
	 * @throws FieldDataInvalidException
	 */
	@Override
	public void setField(final TagField field) throws FieldDataInvalidException {
		id3Tag.setField(field);
	}

	@Override
	public TagField createField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		return id3Tag.createField(genericKey, value);
	}

	@Override
	public String getFirst(final String id) {
		return id3Tag.getFirst(id);
	}

	@Override
	public String getValue(final FieldKey id, final int index) throws KeyNotFoundException {
		return id3Tag.getValue(id, index);
	}

	@Override
	public String getFirst(final FieldKey id) throws KeyNotFoundException {
		return getValue(id, 0);
	}

	@Override
	public TagField getFirstField(final String id) {
		return id3Tag.getFirstField(id);
	}

	@Override
	public TagField getFirstField(final FieldKey genericKey) throws KeyNotFoundException {
		if (genericKey == null)
			throw new KeyNotFoundException();
		else
			return id3Tag.getFirstField(genericKey);
	}

	/**
	 * Delete any instance of tag fields with this key
	 * 
	 * @param fieldKey
	 */
	@Override
	public void deleteField(final FieldKey fieldKey) throws KeyNotFoundException {
		id3Tag.deleteField(fieldKey);
	}

	@Override
	public void deleteField(final String id) throws KeyNotFoundException {
		id3Tag.deleteField(id);
	}

	@Override
	public Iterator<TagField> getFields() {
		return id3Tag.getFields();
	}

	@Override
	public int getFieldCount() {
		return id3Tag.getFieldCount();
	}

	@Override
	public int getFieldCountIncludingSubValues() {
		return getFieldCount();
	}

	@Override
	public boolean setEncoding(final String enc) throws FieldDataInvalidException {
		return id3Tag.setEncoding(enc);
	}

	/**
	 * Create artwork field. Not currently supported.
	 * 
	 */
	@Override
	public TagField createField(final Artwork artwork) throws FieldDataInvalidException {
		throw new FieldDataInvalidException("Not supported");
	}

	@Override
	public void setField(final Artwork artwork) throws FieldDataInvalidException {
		throw new FieldDataInvalidException("Not supported");
	}

	@Override
	public void addField(final Artwork artwork) throws FieldDataInvalidException {
		throw new FieldDataInvalidException("Not supported");
	}

	@Override
	public List<Artwork> getArtworkList() {
		return new ArrayList<Artwork>();
	}

	@Override
	public List<TagField> getFields(final FieldKey id) throws KeyNotFoundException {
		return id3Tag.getFields(id);
	}

	@Override
	public Artwork getFirstArtwork() {
		return null;
	}

	/**
	 * Delete all instance of artwork Field
	 * 
	 * @throws KeyNotFoundException
	 */
	@Override
	public void deleteArtworkField() throws KeyNotFoundException {}

	/**
	 * 
	 * @param genericKey
	 * @return
	 */
	@Override
	public boolean hasField(final FieldKey genericKey) {
		return id3Tag.hasField(genericKey);
	}

	@Override
	public boolean hasField(final String id) {
		return id3Tag.hasField(id);
	}
}
