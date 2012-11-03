package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue222Test extends AbstractTestCase {
	/**
	 * Test read mp4 with meta but not udata atom
	 */
	public void testreadMp4WithoutUUuidButNoUdta() {
		final Path orig = AbstractTestCase.dataPath.resolve("test4.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test4.m4a");

			// Read File okay
			final AudioFile af = AudioFileIO.read(testFile);
			assertTrue(af.getTag().isEmpty()); // But empty
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}
}
