package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

/**
 * Test writing booleans to mp4
 */
public class Issue277Test extends AbstractTestCase {

	/**
	 * Set isCompilation
	 */
	public void testSetIsCompilation() {
		final Path orig = AbstractTestCase.dataPath.resolve("test1.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

			AudioFile af = AudioFileIO.read(testFile);
			assertEquals(0, af.getTag().getFields(FieldKey.IS_COMPILATION).size());

			// Old way
			af.getTag().setField(af.getTag().createField(FieldKey.IS_COMPILATION, "1"));
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals(1, af.getTag().getFields(FieldKey.IS_COMPILATION).size());
			assertEquals("1", af.getTag().getFirst(FieldKey.IS_COMPILATION));

		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Set isCompilation new way
	 */
	public void testSetIsCompilation2() {
		final Path orig = AbstractTestCase.dataPath.resolve("test1.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

			AudioFile af = AudioFileIO.read(testFile);
			assertEquals(0, af.getTag().getFields(FieldKey.IS_COMPILATION).size());

			// Old way
			af.getTag().setField(af.getTag().createField(FieldKey.IS_COMPILATION, "true"));
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals(1, af.getTag().getFields(FieldKey.IS_COMPILATION).size());
			assertEquals("1", af.getTag().getFirst(FieldKey.IS_COMPILATION));

		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Set isCompilation and rating fields
	 */
	public void testSetRating() {
		final Path orig = AbstractTestCase.dataPath.resolve("test1.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

			AudioFile af = AudioFileIO.read(testFile);
			final Mp4Tag tag = (Mp4Tag) af.getTag();
			assertEquals(0, tag.get(Mp4FieldKey.RATING).size());

			// Old way
			af.getTag().setField(tag.createField(Mp4FieldKey.RATING, "1"));
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals(1, tag.get(Mp4FieldKey.RATING).size());
			assertEquals("1", tag.getFirst(Mp4FieldKey.RATING));

		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Set rating is one byte but not true and false so should fail
	 */
	public void testSetRating2() {
		final Path orig = AbstractTestCase.dataPath.resolve("test1.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

			final AudioFile af = AudioFileIO.read(testFile);
			final Mp4Tag tag = (Mp4Tag) af.getTag();
			assertEquals(0, tag.get(Mp4FieldKey.RATING).size());

			af.getTag().setField(tag.createField(Mp4FieldKey.RATING, "true"));

		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNotNull(exceptionCaught);
	}
}
