package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * ID3 Tag Specific flags
 */
public class Issue279Test extends AbstractTestCase {

	/**
	 * Test write to ogg, cant find parent setup header
	 */
	public void testWriteToOgg() {
		final Path orig = AbstractTestCase.dataPath.resolve("test55.ogg");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test55.ogg");

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
