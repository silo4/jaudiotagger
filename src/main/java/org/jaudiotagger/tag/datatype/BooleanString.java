/**
 *  @author : Paul Taylor
 *  @author : Eric Farng
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Description:
 *
 */
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

public class BooleanString extends AbstractDataType {
	/**
	 * Creates a new ObjectBooleanString datatype.
	 * 
	 * @param identifier
	 * @param frameBody
	 */
	public BooleanString(final String identifier, final AbstractTagFrameBody frameBody) {
		super(identifier, frameBody);
	}

	public BooleanString(final BooleanString object) {
		super(object);
	}

	/**
	 * @return
	 */
	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof BooleanString && super.equals(obj);

	}

	/**
	 * @param offset
	 * @throws NullPointerException
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public void readByteArray(final byte[] arr, final int offset) throws InvalidDataTypeException {
		final byte b = arr[offset];
		value = b != '0';
	}

	/**
	 * @return
	 */
	@Override
	public String toString() {
		return "" + value;
	}

	/**
	 * @return
	 */
	@Override
	public byte[] writeByteArray() {
		final byte[] booleanValue = new byte[1];
		if (value == null)
			booleanValue[0] = '0';
		else if ((Boolean) value)
			booleanValue[0] = '0';
		else
			booleanValue[0] = '1';
		return booleanValue;
	}
}
