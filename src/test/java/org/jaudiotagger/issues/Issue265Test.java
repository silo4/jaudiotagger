package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;

/**
 * Truncation haling for string data and picture data
 */
public class Issue265Test extends AbstractTestCase {

	/**
	 * Try an d write too large a file
	 */
	public void testWriteTooLargeStringToFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test7.wma");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception exceptionCaught = null;
		try {
			TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);

			final Path testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
			final AudioFile f = AudioFileIO.read(testFile);
			final Tag tag = f.getTag();
			assertEquals(0, tag.getFields(FieldKey.COVER_ART).size());

			// Now createField artwork field
			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 34000; i++)
				sb.append("x");
			tag.setField(FieldKey.ARTIST, sb.toString());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNotNull(exceptionCaught);
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	/**
	 * Try and write too large a file, automtically truncated if option set
	 */
	public void testWriteTruncateStringToFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test7.wma");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
			final AudioFile f = AudioFileIO.read(testFile);
			final Tag tag = f.getTag();
			assertEquals(0, tag.getFields(FieldKey.COVER_ART).size());

			// Enable value
			TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);

			// Now createField artwork field
			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 34000; i++)
				sb.append("x");
			tag.setField(FieldKey.ARTIST, sb.toString());
			f.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

	/**
	 * Try an d write too large a file
	 */
	public void testWriteTooLargeStringToFileContentDesc() {
		final Path orig = AbstractTestCase.dataPath.resolve("test7.wma");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
			final AudioFile f = AudioFileIO.read(testFile);
			final Tag tag = f.getTag();

			TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);
			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 34000; i++)
				sb.append("x");
			tag.setField(FieldKey.TITLE, sb.toString());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNotNull(exceptionCaught);
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	/**
	 * Try and write too large a file, automtically truncated if option set
	 */
	public void testWriteTruncateStringToFileContentDesc() {
		final Path orig = AbstractTestCase.dataPath.resolve("test7.wma");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
			final AudioFile f = AudioFileIO.read(testFile);
			final Tag tag = f.getTag();

			// Enable value
			TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);

			// Now createField artwork field
			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 34000; i++)
				sb.append("x");
			tag.setField(FieldKey.TITLE, sb.toString());
			f.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

}
