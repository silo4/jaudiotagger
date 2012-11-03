package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;

/**
 * Test handling mp4s that can be read by other apps
 */
public class Issue370Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("test96.m4a");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}
			// ToDO Fix Issue
			// Path testFile = AbstractTestCase.copyAudioToTmp("test96.m4a");
			// AudioFile af = AudioFileIO.read(testFile);
		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
