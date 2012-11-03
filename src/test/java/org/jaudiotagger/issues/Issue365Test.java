package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test reading an ogg file with ID3 tag at start
 */
public class Issue365Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("test90.ogg");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("test90.ogg");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(FieldKey.ARTIST, "fred");
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("fred", af.getTag().getFirst(FieldKey.ARTIST));
		} catch (final Exception e) {
			e.printStackTrace();
			caught = e;
		}
		assertNull(caught);
	}
}
