package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v11Tag;

/**
 * Test deletions of ID3v1 tag
 */
public class Issue324Test extends AbstractTestCase {
	public void testID3v1TagHandling() throws Exception {

		final Path orig = AbstractTestCase.dataPath.resolve("test32.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		final Path testFile = AbstractTestCase.copyAudioToTmp("test32.mp3");
		assertEquals(1853744, Files.size(testFile));
		MP3File f = (MP3File) AudioFileIO.read(testFile);
		assertEquals("Iron Maiden", f.getID3v1Tag().getFirst(FieldKey.ARTIST));
		f.setID3v1Tag(new ID3v11Tag());
		f.getID3v1Tag().setField(FieldKey.ARTIST, "Iron Mask");
		f.commit();
		assertEquals(1853744, Files.size(testFile));
		f = (MP3File) AudioFileIO.read(testFile);
		assertEquals("Iron Mask", f.getID3v1Tag().getFirst(FieldKey.ARTIST));

	}
}
