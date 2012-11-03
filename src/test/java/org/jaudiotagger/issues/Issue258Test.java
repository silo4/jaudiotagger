package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Test Creating Temp file when filename < 3
 */
public class Issue258Test extends AbstractTestCase {
	/**
	 * Test write of mp3 with very short filename
	 */
	public void testWriteToShortMp3File() {
		final Path orig = AbstractTestCase.dataPath.resolve("01.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("01.mp3");

			// Read File, and write tag cause padding to be adjusted and temp file created
			final AudioFile af = AudioFileIO.read(testFile);
			final Tag t = af.getTagOrCreateAndSetDefault();
			t.setField(FieldKey.ARTIST, "fred");
			af.commit();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test write to mp4 with very short file name
	 */
	public void testWriteToShortMp4File() {
		final Path orig = AbstractTestCase.dataPath.resolve("01.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("01.m4a");

			// Read File
			final AudioFile af = AudioFileIO.read(testFile);
			final Tag t = af.getTagOrCreateAndSetDefault();
			t.setField(FieldKey.ARTIST, "fred");
			af.commit();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}
}
