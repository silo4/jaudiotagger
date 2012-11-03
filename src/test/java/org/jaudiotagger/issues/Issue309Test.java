package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Reading mp4 with corrupt length recorded in tag ending up in middle of free atom should fail
 */
public class Issue309Test extends AbstractTestCase {
	public static int countExceptions = 0;

	public void testAddingLargeImageToOgg() throws Exception {
		final Path orig = AbstractTestCase.dataPath.resolve("test73.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test73.m4a");
			final AudioFile af = AudioFileIO.read(testFile);

		} catch (final Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
		assertNotNull(e);
	}
}
