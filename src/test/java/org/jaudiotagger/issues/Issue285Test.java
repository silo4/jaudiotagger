package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Converting FrameBodyUnsupported with known identifier to FrameBodyIPLS (v23) causing NoSuchMethodException. Not
 * really sure why this is happening but we should check and take action instead of failing as we currently do
 */
public class Issue285Test extends AbstractTestCase {
	public void testSavingOggFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test57.ogg");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test57.ogg");

			// OggFileReader ofr = new OggFileReader();
			// ofr.summarizeOggPageHeaders(testFile);

			final AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(FieldKey.COMMENT, "TEST");
			af.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
