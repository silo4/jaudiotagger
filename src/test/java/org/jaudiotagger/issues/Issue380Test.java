package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test reading of track without total for mp4
 */
public class Issue380Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("test98.m4a");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("test98.m4a");
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals("", af.getTag().getFirst(FieldKey.TRACK_TOTAL));
		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
