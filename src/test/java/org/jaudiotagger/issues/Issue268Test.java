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
public class Issue268Test extends AbstractTestCase {

	/**
	 * Test read wma with NonArtwork Binary Data
	 */
	public void testReadWma() {
		final Path orig = AbstractTestCase.dataPath.resolve("test8.wma");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test8.wma");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			System.out.println(af.getTag().toString());

			af.getTag().setField(FieldKey.ALBUM, "FRED");
			af.commit();
			af = AudioFileIO.read(testFile);
			System.out.println(af.getTag().toString());
			assertEquals("FRED", af.getTag().getFirst(FieldKey.ALBUM));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
