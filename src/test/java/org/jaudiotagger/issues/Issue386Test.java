package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Test reading of track without total for mp4
 */
public class Issue386Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("test99.mp3");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("test99.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			System.out.println(af.getAudioHeader());
		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
