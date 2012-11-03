package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test can read FlacTag with spec breaking PICTUREBLOCK as first block and then write chnages to it reordering so that
 * STREAMINFO is the first block
 */
public class Issue315Test extends AbstractTestCase {
	/*
	 * 
	 * @throws Exception
	 */
	public void testReadWriteTagWithPictureBlockAtStart() throws Exception {
		final Path orig = AbstractTestCase.dataPath.resolve("test54.flac");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test54.flac");
			AudioFile af = AudioFileIO.read(testFile);

			// Modify File
			af.getTag().setField(FieldKey.TITLE, "newtitle");
			af.commit();

			// Reread File
			af = AudioFileIO.read(testFile);

		} catch (final Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
		assertNull(e);
	}
}
