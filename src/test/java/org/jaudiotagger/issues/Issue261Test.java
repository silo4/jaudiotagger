package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue261Test extends AbstractTestCase {

	/**
	 * Test write mp4 ok without any udta/meta atoms
	 */
	public void testWriteMp4() {
		final Path orig = AbstractTestCase.dataPath.resolve("test45.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test45.m4a");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);

			// Write file
			af.getTag().setField(FieldKey.YEAR, "2007");
			af.commit();

			af = AudioFileIO.read(testFile);
			assertEquals("2007", af.getTag().getFirst(FieldKey.YEAR));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
