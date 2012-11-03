package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test reading of TIPL frame where the 2nd field of last pairing is not null terminated
 */
public class IssueTrackTotalTest extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			// System.out.println("TrackTotal Loading to Database:"+audioFile.getTagOrCreateDefault().getFirst(FieldKey.TRACK_TOTAL)+":");

			final Path orig = AbstractTestCase.dataPath.resolve("issue400.mp3");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("issue400.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			final MP3File mp3 = (MP3File) af;
			assertNotNull(mp3.getID3v2Tag());
			assertNotNull(af.getTag().getFirst(FieldKey.TRACK_TOTAL));
			assertEquals("", af.getTag().getFirst(FieldKey.TRACK_TOTAL));
			assertEquals("", af.getTagOrCreateDefault().getFirst(FieldKey.TRACK_TOTAL));

		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
