package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * File corrupt after write
 */
public class Issue290Test extends AbstractTestCase {
	public void testSavingFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test59.mp4");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test59.mp4");
			AudioFile af = AudioFileIO.read(testFile);
			System.out.println("Tag is" + af.getTag().toString());
			af.getTag().setField(af.getTag().createField(FieldKey.ARTIST, "fred"));
			af.commit();

			af = AudioFileIO.read(testFile);
			assertEquals("fred", af.getTag().getFirst(FieldKey.ARTIST));
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);

	}

}
