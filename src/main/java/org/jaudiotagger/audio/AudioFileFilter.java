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
package org.jaudiotagger.audio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.audio.generic.Utils;

/**
 * <p>
 * This is a simple FileFilter that will only allow the file supported by this library.
 * </p>
 * <p>
 * It will also accept directories. An additional condition is that file must be readable (read permission) and are not
 * hidden (dot files, or hidden files)
 * </p>
 * 
 * @author Raphael Slinckx
 * @version $Id$
 * @since v0.01
 */
public class AudioFileFilter implements DirectoryStream.Filter<Path> {
	/**
	 * allows Directories
	 */
	private final boolean allowDirectories;

	public AudioFileFilter(final boolean allowDirectories) {
		this.allowDirectories = allowDirectories;
	}

	public AudioFileFilter() {
		this(true);
	}

	/**
	 * <p>
	 * Check whether the given file meet the required conditions (supported by the library OR directory). The File must
	 * also be readable and not hidden.
	 * </p>
	 * 
	 * @param f
	 *            The file to test
	 * @return a boolean indicating if the file is accepted or not
	 */
	@Override
	public boolean accept(final Path f) {
		try {
			if (Files.isHidden(f) || !Files.isReadable(f))
				return false;
		} catch (final IOException e) {
			return false;
		}

		if (Files.isDirectory(f))
			return allowDirectories;

		final String ext = Utils.getExtension(f);

		try {
			if (SupportedFileFormat.valueOf(ext.toUpperCase()) != null)
				return true;
		} catch (final IllegalArgumentException iae) {
			// Not known enum value
			return false;
		}
		return false;
	}
}
