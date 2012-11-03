package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;

/**
 * Test Writing to new urls with common interface
 */
public class Issue257Test extends AbstractTestCase {
	/**
	 * Test Mp4 with crap between free atom and mdat atom, shoud cause immediate failure
	 */
	public void testReadMp4FileWithPaddingAfterLastAtom() {
		final Path orig = AbstractTestCase.dataPath.resolve("test37.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test37.m4a");

			// Read File
			final AudioFile af = AudioFileIO.read(testFile);

			// Print Out Tree

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertTrue(exceptionCaught instanceof CannotReadException);
	}
}
