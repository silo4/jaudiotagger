package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;

/**
 * Test read large mp3 with extended header
 */
public class Issue270Test extends AbstractTestCase {

	/**
	 * Test read mp3 that says it has extended header but doesnt really
	 */
	public void testReadMp4WithCorruptMdata() {
		final Path orig = AbstractTestCase.dataPath.resolve("test49.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test49.m4a");

			// Read FileFails
			final AudioFile af = AudioFileIO.read(testFile);
			System.out.println(af.getTag().toString());
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertTrue(exceptionCaught instanceof CannotReadException);
	}

}
