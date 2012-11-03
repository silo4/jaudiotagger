package org.jaudiotagger.issues;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

/**
 * Test Writing to mp4 with top level free data atoms but free atoms and mdat are before ilst so not useful
 */
public class Issue240Test extends AbstractTestCase {
	public void testWritelargeDataToFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test34.m4a");
		if (!Files.isRegularFile(orig))
			return;

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test34.m4a");

			AudioFile af = AudioFileIO.read(testFile);
			assertEquals(0, ((Mp4Tag) af.getTag()).getFields(FieldKey.COVER_ART).size());

			// Add new image
			final RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
			final byte[] imagedata = new byte[(int) imageFile.length()];
			imageFile.read(imagedata);
			af.getTag().addField(((Mp4Tag) af.getTag()).createArtworkField(imagedata));
			af.commit();

			// Read File back
			af = AudioFileIO.read(testFile);
			assertEquals(1, ((Mp4Tag) af.getTag()).getFields(FieldKey.COVER_ART).size());
		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}
}
