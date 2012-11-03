package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Flac Reading
 */
public class Issue274Test extends AbstractTestCase {

	/**
	 * Test Flac
	 */
	public void testReadFlac() {
		final Path orig = AbstractTestCase.dataPath.resolve("test54.flac");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test54.flac");

			// Read File okay
			final AudioFile af = AudioFileIO.read(testFile);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);

	}

}
