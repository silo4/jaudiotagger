/*
 * jaudiotagger library
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.images.Artwork;

/**
 * This class is the default implementation for {@link org.jaudiotagger.tag.Tag} and introduces some more useful
 * functionality to be implemented.<br>
 * 
 * @author Raphaël Slinckx
 */
public abstract class AbstractTag implements Tag {
	/**
	 * Stores the amount of {@link TagField} with {@link TagField#isCommon()} <code>true</code>.
	 */
	protected int commonNumber = 0;

	/**
	 * This map stores the {@linkplain TagField#getId() ids} of the stored fields to the {@linkplain TagField fields}
	 * themselves. Because a linked hashMap is used the order that they are added in is preserved, the only exception to
	 * this rule is when two fields of the same id exist, both will be returned according to when the first item was
	 * added to the file. <br>
	 */
	protected Map<String, List<TagField>> fields = new LinkedHashMap<String, List<TagField>>();

	/**
	 * Add field
	 * 
	 * @see org.jaudiotagger.tag.Tag#addField(org.jaudiotagger.tag.TagField) <p/>
	 *      Changed so add empty fields
	 */
	@Override
	public void addField(final TagField field) {
		if (field == null)
			return;

		List<TagField> list = fields.get(field.getId());

		// There was no previous item
		if (list == null) {
			list = new ArrayList<TagField>();
			list.add(field);
			fields.put(field.getId(), list);
			if (field.isCommon())
				commonNumber++;
		} else
			// We append to existing list
			list.add(field);
	}

	/**
	 * Get list of fields within this tag with the specified id
	 * 
	 * @see org.jaudiotagger.tag.Tag#getFields(java.lang.String)
	 */
	@Override
	public List<TagField> getFields(final String id) {
		final List<TagField> list = fields.get(id);

		if (list == null)
			return new ArrayList<TagField>();

		return list;
	}

	public List<String> getAll(final String id) throws KeyNotFoundException {
		final List<String> fields = new ArrayList<String>();
		final List<TagField> tagFields = getFields(id);
		for (final TagField tagField : tagFields)
			fields.add(tagField.toString());
		return fields;
	}

	/**
	 * 
	 * @param id
	 * @param index
	 * @return
	 */
	public String getItem(final String id, final int index) {
		final List<TagField> l = getFields(id);
		return (l.size() > index) ? l.get(index).toString() : "";
	}

	/**
	 * Retrieve the first value that exists for this generic key
	 * 
	 * @param genericKey
	 * @return
	 */
	@Override
	public String getFirst(final FieldKey genericKey) throws KeyNotFoundException {
		return getValue(genericKey, 0);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public String getFirst(final String id) {
		final List<TagField> l = getFields(id);
		return (l.size() != 0) ? l.get(0).toString() : "";
	}

	/**
	 * 
	 * @param id
	 *            audio specific key
	 * @return
	 */
	@Override
	public TagField getFirstField(final String id) {
		final List<TagField> l = getFields(id);
		return (l.size() != 0) ? l.get(0) : null;
	}

	/**
	 * @see org.jaudiotagger.tag.Tag#getFields()
	 */
	@Override
	public Iterator<TagField> getFields() {
		final Iterator<Map.Entry<String, List<TagField>>> it = this.fields.entrySet().iterator();
		return new Iterator<TagField>() {
			private Iterator<TagField> fieldsIt;

			private void changeIt() {
				if (!it.hasNext())
					return;

				final Map.Entry<String, List<TagField>> e = it.next();
				final List<TagField> l = e.getValue();
				fieldsIt = l.iterator();
			}

			@Override
			public boolean hasNext() {
				if (fieldsIt == null)
					changeIt();
				return it.hasNext() || (fieldsIt != null && fieldsIt.hasNext());
			}

			@Override
			public TagField next() {
				if (!fieldsIt.hasNext())
					changeIt();

				return fieldsIt.next();
			}

			@Override
			public void remove() {
				fieldsIt.remove();
			}
		};
	}

	/**
	 * Return field count
	 * <p/>
	 * TODO:There must be a more efficient way to do this.
	 * 
	 * @return field count
	 */
	@Override
	public int getFieldCount() {
		final Iterator<TagField> it = getFields();
		int count = 0;
		while (it.hasNext()) {
			count++;
			it.next();
		}
		return count;
	}

	@Override
	public int getFieldCountIncludingSubValues() {
		return getFieldCount();
	}

	/**
	 * Does this tag contain any comon fields
	 * 
	 * @see org.jaudiotagger.tag.Tag#hasCommonFields()
	 */
	@Override
	public boolean hasCommonFields() {
		return commonNumber != 0;
	}

	/**
	 * Does this tag contain a field with the specified id
	 * 
	 * @see org.jaudiotagger.tag.Tag#hasField(java.lang.String)
	 */
	@Override
	public boolean hasField(final String id) {
		return getFields(id).size() != 0;
	}

	@Override
	public boolean hasField(final FieldKey fieldKey) {
		return hasField(fieldKey.name());
	}

	/**
	 * Determines whether the given charset encoding may be used for the represented tagging system.
	 * 
	 * @param enc
	 *            charset encoding.
	 * @return <code>true</code> if the given encoding can be used.
	 */
	protected abstract boolean isAllowedEncoding(String enc);

	/**
	 * Is this tag empty
	 * 
	 * @see org.jaudiotagger.tag.Tag#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return fields.size() == 0;
	}

	/**
	 * Create new field and set it in the tag
	 * 
	 * @param genericKey
	 * @param value
	 * @throws KeyNotFoundException
	 * @throws FieldDataInvalidException
	 */
	@Override
	public void setField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		final TagField tagfield = createField(genericKey, value);
		setField(tagfield);
	}

	/**
	 * Create new field and add it to the tag
	 * 
	 * @param genericKey
	 * @param value
	 * @throws KeyNotFoundException
	 * @throws FieldDataInvalidException
	 */
	@Override
	public void addField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		final TagField tagfield = createField(genericKey, value);
		addField(tagfield);
	}

	/**
	 * Set field
	 * <p/>
	 * Changed:Just because field is empty it doesn't mean it should be deleted. That should be the choice of the
	 * developer. (Or does this break things)
	 * 
	 * @see org.jaudiotagger.tag.Tag#setField(org.jaudiotagger.tag.TagField)
	 */
	@Override
	public void setField(final TagField field) {
		if (field == null)
			return;

		// If there is already an existing field with same id
		// and both are TextFields, we replace the first element
		List<TagField> list = fields.get(field.getId());
		if (list != null) {
			list.set(0, field);
			return;
		}

		// Else we put the new field in the fields.
		list = new ArrayList<TagField>();
		list.add(field);
		fields.put(field.getId(), list);
		if (field.isCommon())
			commonNumber++;
	}

	/**
	 * Set or add encoding
	 * 
	 * @see org.jaudiotagger.tag.Tag#setEncoding(java.lang.String)
	 */
	@Override
	public boolean setEncoding(final String enc) {
		if (!isAllowedEncoding(enc))
			return false;

		final Iterator<TagField> it = getFields();
		while (it.hasNext()) {
			final TagField field = it.next();
			if (field instanceof TagTextField)
				((TagTextField) field).setEncoding(enc);
		}

		return true;
	}

	/**
	 * (overridden)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer out = new StringBuffer();
		out.append("Tag content:\n");
		final Iterator<TagField> it = getFields();
		while (it.hasNext()) {
			final TagField field = it.next();
			out.append("\t");
			out.append(field.getId());
			out.append(":");
			out.append(field.toString());
			out.append("\n");
		}
		return out.toString().substring(0, out.length() - 1);
	}

	/**
	 * 
	 * @param genericKey
	 * @param value
	 * @return
	 * @throws KeyNotFoundException
	 * @throws FieldDataInvalidException
	 */
	@Override
	public abstract TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException;

	/**
	 * 
	 * @param genericKey
	 * @return
	 * @throws KeyNotFoundException
	 */
	@Override
	public abstract TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException;

	/**
	 * 
	 * @param fieldKey
	 * @throws KeyNotFoundException
	 */
	@Override
	public abstract void deleteField(FieldKey fieldKey) throws KeyNotFoundException;

	/**
	 * Delete all occurrences of field with this id.
	 * 
	 * @param key
	 */
	@Override
	public void deleteField(final String key) {
		fields.remove(key);
	}

	@Override
	public Artwork getFirstArtwork() {
		final List<Artwork> artwork = getArtworkList();
		if (artwork.size() > 0)
			return artwork.get(0);
		return null;
	}

	/**
	 * Create field and then set within tag itself
	 * 
	 * @param artwork
	 * @throws FieldDataInvalidException
	 */
	@Override
	public void setField(final Artwork artwork) throws FieldDataInvalidException {
		this.setField(createField(artwork));
	}

	/**
	 * Create field and then add within tag itself
	 * 
	 * @param artwork
	 * @throws FieldDataInvalidException
	 */
	@Override
	public void addField(final Artwork artwork) throws FieldDataInvalidException {
		this.addField(createField(artwork));
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
}
