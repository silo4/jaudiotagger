package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * This test is incomplete
 * 
 */
public class Issue404Test extends AbstractTestCase {
	public void testWritingTooLongTempFile() throws Exception {
		final Path origFile = Paths.get("testdata",
				"test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a");
		if (!Files.isRegularFile(origFile)) {
			System.err.println("Unable to test file " + origFile.toAbsolutePath() + " - not available");
			return;
		}

		Exception caught = null;
		try {
			final Path orig = AbstractTestCase
					.copyAudioToTmp("test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a");
			final AudioFile af = AudioFileIO.read(orig);
			af.getTag().setField(FieldKey.ALBUM, "Albumstuff");
			AudioFileIO.write(af);
		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
