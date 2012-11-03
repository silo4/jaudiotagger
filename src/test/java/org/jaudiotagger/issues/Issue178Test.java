package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;

/**
 * Test Fail bad Ogg Quicker
 */
public class Issue178Test extends AbstractTestCase {
	/**
	 * Test Read empty file pretenidng to be an Ogg, should fail quickly
	 */
	public void testReadBadOgg() {
		final Path orig = AbstractTestCase.dataPath.resolve("test36.ogg");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		final Date startDate = new Date();
		System.out.println("start:" + startDate);
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test36.ogg");

			// Read File
			final AudioFile af = AudioFileIO.read(testFile);

			// Print Out Tree

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		final Date endDate = new Date();
		System.out.println("end  :" + endDate);
		assertTrue(exceptionCaught instanceof CannotReadException);
		assertTrue(endDate.getTime() - startDate.getTime() < 1000);
	}
}
