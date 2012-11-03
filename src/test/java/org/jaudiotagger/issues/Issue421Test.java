package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Hide the differences between the two genre fields used by the mp4 format
 */
public class Issue421Test extends AbstractTestCase {
	public void testTrackField() throws Exception {
		final Path orig = AbstractTestCase.dataPath.resolve("Arizona.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		final Path testFile = AbstractTestCase.copyAudioToTmp("Arizona.m4a");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTag();
		assertEquals("13", tag.getFirst(FieldKey.TRACK));
		assertEquals("14", tag.getFirst(FieldKey.TRACK_TOTAL));
		assertEquals("13", tag.getAll(FieldKey.TRACK).get(0));
		assertEquals("14", tag.getAll(FieldKey.TRACK_TOTAL).get(0));
	}
}
