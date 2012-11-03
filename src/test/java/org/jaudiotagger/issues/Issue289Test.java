package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.tag.FieldKey;

/**
 * File corrupt after write
 */
public class Issue289Test extends AbstractTestCase {
	public void testSavingOggFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test58.ogg");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test58.ogg");

			final OggFileReader ofr = new OggFileReader();
			// ofr.shortSummarizeOggPageHeaders(testFile);

			AudioFile af = AudioFileIO.read(testFile);
			System.out.println(af.getTag().toString());
			af.getTag().setField(af.getTag().createField(FieldKey.MUSICIP_ID, "91421a81-50b9-f577-70cf-20356eea212e"));
			af.commit();

			af = AudioFileIO.read(testFile);
			assertEquals("91421a81-50b9-f577-70cf-20356eea212e", af.getTag().getFirst(FieldKey.MUSICIP_ID));

			ofr.shortSummarizeOggPageHeaders(testFile);
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
