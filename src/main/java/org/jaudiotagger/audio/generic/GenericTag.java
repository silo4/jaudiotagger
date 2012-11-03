/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
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
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.images.Artwork;

/**
 * This is a complete example implementation of {@link AbstractTag} and it currenlty used to provide basic support to
 * audio formats with only read tagging ability such as Real or Wav files <br>
 * 
 * @author Raphaël Slinckx
 */
public abstract class GenericTag extends AbstractTag {
	private static EnumSet<FieldKey> supportedKeys;

	static {
		supportedKeys = EnumSet.of(FieldKey.ALBUM, FieldKey.ARTIST, FieldKey.TITLE, FieldKey.TRACK, FieldKey.GENRE, FieldKey.COMMENT, FieldKey.YEAR);
	}

	/**
	 * Implementations of {@link TagTextField} for use with &quot;ISO-8859-1&quot; strings.
	 * 
	 * @author Raphaël Slinckx
	 */
	private class GenericTagTextField implements TagTextField {

		/**
		 * Stores the string.
		 */
		private String content;

		/**
		 * Stores the identifier.
		 */
		private final String id;

		/**
		 * Creates an instance.
		 * 
		 * @param fieldId
		 *            The identifier.
		 * @param initialContent
		 *            The string.
		 */
		public GenericTagTextField(final String fieldId, final String initialContent) {
			this.id = fieldId;
			this.content = initialContent;
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#copyContent(org.jaudiotagger.tag.TagField)
		 */
		@Override
		public void copyContent(final TagField field) {
			if (field instanceof TagTextField)
				this.content = ((TagTextField) field).getContent();
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagTextField#getContent()
		 */
		@Override
		public String getContent() {
			return this.content;
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagTextField#getEncoding()
		 */
		@Override
		public String getEncoding() {
			return "ISO-8859-1";
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#getId()
		 */
		@Override
		public String getId() {
			return id;
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#getRawContent()
		 */
		@Override
		public byte[] getRawContent() {
			return this.content == null ? new byte[] {} : Utils.getDefaultBytes(this.content, getEncoding());
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#isBinary()
		 */
		@Override
		public boolean isBinary() {
			return false;
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#isBinary(boolean)
		 */
		@Override
		public void isBinary(final boolean b) {
			/* not supported */
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#isCommon()
		 */
		@Override
		public boolean isCommon() {
			return true;
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagField#isEmpty()
		 */
		@Override
		public boolean isEmpty() {
			return this.content.equals("");
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagTextField#setContent(java.lang.String)
		 */
		@Override
		public void setContent(final String s) {
			this.content = s;
		}

		/**
		 * (overridden)
		 * 
		 * @see org.jaudiotagger.tag.TagTextField#setEncoding(java.lang.String)
		 */
		@Override
		public void setEncoding(final String s) {
			/* Not allowed */
		}

		/**
		 * (overridden)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getContent();
		}
	}

	/**
	 * (overridden)
	 * 
	 * @see org.jaudiotagger.audio.generic.AbstractTag#isAllowedEncoding(java.lang.String)
	 */
	@Override
	protected boolean isAllowedEncoding(final String enc) {
		return true;
	}

	@Override
	public TagField createField(final FieldKey genericKey, final String value) throws KeyNotFoundException, FieldDataInvalidException {
		if (supportedKeys.contains(genericKey))
			return new GenericTagTextField(genericKey.name(), value);
		else
			throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
	}

	/**
	 * @param genericKey
	 * @return
	 * @throws KeyNotFoundException
	 */
	@Override
	public String getFirst(final FieldKey genericKey) throws KeyNotFoundException {
		return getValue(genericKey, 0);
	}

	@Override
	public String getValue(final FieldKey genericKey, final int index) throws KeyNotFoundException {
		if (supportedKeys.contains(genericKey))
			return getItem(genericKey.name(), index);
		else
			throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
	}

	/**
	 * 
	 * @param genericKey
	 *            The field id.
	 * @return
	 * @throws KeyNotFoundException
	 */
	@Override
	public List<TagField> getFields(final FieldKey genericKey) throws KeyNotFoundException {
		final List<TagField> list = fields.get(genericKey.name());
		if (list == null)
			return new ArrayList<TagField>();
		return list;
	}

	@Override
	public List<String> getAll(final FieldKey genericKey) throws KeyNotFoundException {
		return super.getAll(genericKey.name());
	}

	/**
	 * @param genericKey
	 * @throws KeyNotFoundException
	 */
	@Override
	public void deleteField(final FieldKey genericKey) throws KeyNotFoundException {
		if (supportedKeys.contains(genericKey))
			deleteField(genericKey.name());
		else
			throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
	}

	/**
	 * @param genericKey
	 * @return
	 * @throws KeyNotFoundException
	 */
	@Override
	public TagField getFirstField(final FieldKey genericKey) throws KeyNotFoundException {
		if (supportedKeys.contains(genericKey))
			return getFirstField(genericKey.name());
		else
			throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
	}

	@Override
	public List<Artwork> getArtworkList() {
		return Collections.emptyList();
	}

	@Override
	public TagField createField(final Artwork artwork) throws FieldDataInvalidException {
		throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
	}
}
