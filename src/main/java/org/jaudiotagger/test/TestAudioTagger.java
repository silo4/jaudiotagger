/**
 * @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * <p/>
 * Jaudiotagger Copyright (C)2004,2005
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 * Description:
 */
package org.jaudiotagger.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Simple class that will attempt to recursively read all files within a directory, flags errors that occur.
 */
public class TestAudioTagger {

	public static void main(final String[] args) {
		final TestAudioTagger test = new TestAudioTagger();

		if (args.length == 0) {
			System.err.println("usage TestAudioTagger Dirname");
			System.err.println("      You must enter the root directory");
			System.exit(1);
		} else if (args.length > 1) {
			System.err.println("usage TestAudioTagger Dirname");
			System.err.println("      Only one parameter accepted");
			System.exit(1);
		}
		final Path rootDir = Paths.get(args[0]);
		if (!Files.isDirectory(rootDir)) {
			System.err.println("usage TestAudioTagger Dirname");
			System.err.println("      Directory " + args[0] + " could not be found");
			System.exit(1);
		}
		final Date start = new Date();
		System.out.println("Started to read from:" + rootDir + " at " + DateFormat.getTimeInstance().format(start));
		test.scanSingleDir(rootDir);
		final Date finish = new Date();
		System.out.println("Started to read from:" + rootDir + " at " + DateFormat.getTimeInstance().format(start));
		System.out.println("Finished to read from:" + rootDir + DateFormat.getTimeInstance().format(finish));
		System.out.println("Attempted  to read:" + count);
		System.out.println("Successful to read:" + (count - failed));
		System.out.println("Failed     to read:" + failed);

	}

	private static int count = 0;
	private static int failed = 0;

	/**
	 * Recursive function to scan directory
	 * 
	 * @param dir
	 */
	private void scanSingleDir(final Path dir) {
		try (DirectoryStream<Path> audioFilesStream = Files.newDirectoryStream(dir, new AudioFileFilter(false))) {
			final Iterator<Path> audioFiles = audioFilesStream.iterator();
			while (audioFiles.hasNext()) {
				final Path audioFile = audioFiles.next();
				count++;
				try {
					AudioFileIO.read(audioFile);
				} catch (final Throwable t) {
					System.err.println("Unable to read record:" + count + ":" + audioFile);
					failed++;
					t.printStackTrace();
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try (DirectoryStream<Path> audioFileDirsStream = Files.newDirectoryStream(dir, new DirFilter())) {
			final Iterator<Path> audioFileDirs = audioFileDirsStream.iterator();
			while (audioFileDirs.hasNext()) {
				final Path audioFileDir = audioFileDirs.next();
				scanSingleDir(audioFileDir);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public final class DirFilter implements DirectoryStream.Filter<Path> {
		public DirFilter() {}

		/**
		 * Determines whether or not the file is an mp3 file. If the file is a directory, whether or not is accepted
		 * depends upon the allowDirectories flag passed to the constructor.
		 * 
		 * @param file
		 *            the file to test
		 * @return true if this file or directory should be accepted
		 */
		@Override
		public final boolean accept(final Path file) {
			return Files.isDirectory(file);
		}

		public static final String IDENT = "$Id$";
	}
}
