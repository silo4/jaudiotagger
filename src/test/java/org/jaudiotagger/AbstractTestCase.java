/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jaudiotagger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.EnumMap;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.TagOptionSingleton;

/**
 *
 */
public abstract class AbstractTestCase extends TestCase {

	@Override
	public void setUp() {
		TagOptionSingleton.getInstance().setToDefault();
	}

	/**
	 * Stores a {@link Pattern} for each {@link ErrorMessage}.<br>
	 * Place holders like &quot;{&lt;number&gt;}&quot; will be replaced with &quot;.*&quot;.<br>
	 */
	private final static EnumMap<ErrorMessage, Pattern> ERROR_PATTERNS;

	static {
		ERROR_PATTERNS = new EnumMap<ErrorMessage, Pattern>(ErrorMessage.class);
		for (final ErrorMessage curr : ErrorMessage.values()) {
			final String regex = curr.getMsg().replaceAll("\\{\\d+\\}", ".*");
			ERROR_PATTERNS.put(curr, Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
		}
	}

	private static boolean append(final Path inputTagFile, final Path inputFile, final Path outputFile) {
		try {
			final FileInputStream in = new FileInputStream(inputTagFile.toFile());
			final FileInputStream in2 = new FileInputStream(inputFile.toFile());
			final FileOutputStream out = new FileOutputStream(outputFile.toFile());
			final BufferedInputStream inBuffer = new BufferedInputStream(in);
			final BufferedInputStream inBuffer2 = new BufferedInputStream(in2);
			final BufferedOutputStream outBuffer = new BufferedOutputStream(out);

			int theByte;

			while ((theByte = inBuffer.read()) > -1)
				outBuffer.write(theByte);

			while ((theByte = inBuffer2.read()) > -1)
				outBuffer.write(theByte);

			outBuffer.close();
			inBuffer.close();
			inBuffer2.close();
			out.close();
			in.close();
			in2.close();

			// cleanup if files are not the same length
			if ((Files.size(inputTagFile) + Files.size(inputFile)) != Files.size(outputFile)) {
				outputFile.toFile().delete();

				return false;
			}

			return true;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Copy a File
	 * 
	 * @param fromFile
	 *            The existing File
	 * @param toFile
	 *            The new File
	 * @return <code>true</code> if and only if the renaming succeeded; <code>false</code> otherwise
	 */
	public static boolean copy(final Path fromPath, final Path toPath) {
		// final File fromFile = fromPath.toFile();
		// final File toFile = toPath.toFile();
		try {

			Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);

			// final FileInputStream in = new FileInputStream(fromFile);
			// final FileOutputStream out = new FileOutputStream(toFile);
			// final byte[] buf = new byte[8192];
			//
			// int len;
			//
			// while ((len = in.read(buf)) > -1)
			// out.write(buf, 0, len);
			//
			// in.close();
			// out.close();
			//
			// // cleanup if files are not the same length
			// if (fromFile.length() != toFile.length()) {
			// toFile.delete();
			//
			// return false;
			// }

			return true;
		} catch (final IOException e) {
			System.err.println("copy(final Path fromPath, final Path toPath)");
			try {
				Files.deleteIfExists(toPath);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Copy audiofile to processing dir ready for use in test
	 * 
	 * @param fileName
	 * @return
	 */
	public static Path copyAudioToTmp(final String fileName) {
		final Path inputFile = dataPath.resolve(fileName);
		final Path outputFile = dataTempPath.resolve(fileName);
		final Path parentFile = outputFile.getParent();
		if (!Files.exists(parentFile))
			try {
				Files.createDirectory(parentFile);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		final boolean result = copy(inputFile, outputFile);
		assertTrue(result);
		return outputFile;
	}

	/**
	 * Copy audiofile to processing dir ready for use in test, use this if using same file in multiple tests because
	 * with junit multithreading can have problems otherwise
	 * 
	 * @param fileName
	 * @return
	 */
	public static Path copyAudioToTmp(final String fileName, final Path newFileName) {
		final Path inputFile = dataPath.resolve(fileName);
		final Path outputFile = dataTempPath.resolve(newFileName);
		final Path parentFile = outputFile.getParent();
		if (!Files.exists(parentFile))
			try {
				Files.createDirectory(parentFile);
			} catch (final IOException e) {
				System.err.println("copyAudioToTmp failed!");
				System.err.println(inputFile.toAbsolutePath());
				System.err.println(outputFile.toAbsolutePath());
				e.printStackTrace();
			}
		final boolean result = copy(inputFile, outputFile);
		assertTrue(result);
		return outputFile;
	}

	/**
	 * Prepends file with tag file in order to create an mp3 with a valid id3
	 * 
	 * @param tagfile
	 * @param fileName
	 * @return
	 */
	public static Path copyAudioToTmp(final String tagfile, final String fileName) {
		final Path inputTagFile = tagsPath.resolve(tagfile);
		final Path inputFile = dataPath.resolve(fileName);
		final Path outputFile = dataTempPath.resolve(fileName);
		if (!Files.exists(outputFile.getParent()))
			outputFile.getParent().toFile().mkdirs();
		final boolean result = append(inputTagFile, inputFile, outputFile);
		assertTrue(result);
		return outputFile;
	}

	/**
	 * This method asserts that the given <code>actual</code> message is constructed with the <code>expected</code>
	 * message string.<br>
	 * <br>
	 * 
	 * @param expected
	 *            the expected message source.
	 * @param actual
	 *            the message to compare against.
	 */
	public void assertErrorMessage(final ErrorMessage expected, final String actual) {
		assertTrue("Message not correctly constructed.", ERROR_PATTERNS.get(expected).matcher(actual).matches());
	}

	public static final Path testResourcesPath = Paths.get("src/test/resources/");
	public static final Path dataPath = testResourcesPath.resolve("data/");
	public static final Path dataTempPath = testResourcesPath.resolve("datatmp/");
	public static final Path tagsPath = testResourcesPath.resolve("tags/");
}
