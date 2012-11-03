package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;

/**
 * Test if read a tag with a corrupt frame that in certain circumstances continue to read the other frames regardless.
 */
public class Issue250Test extends AbstractTestCase {
	public void testReadingFileWithCorruptFirstFrame() throws Exception {
		final Path orig = AbstractTestCase.dataPath.resolve("test78.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}
		final Path testFile = AbstractTestCase.copyAudioToTmp("test78.mp3");

		MP3File f = (MP3File) AudioFileIO.read(testFile);
		Tag tag = f.getTag();
		assertTrue(f.getTag() instanceof ID3v23Tag);
		ID3v23Tag id3v23tag = (ID3v23Tag) tag;
		// Dodgy frame is not counted
		assertEquals(13, id3v23tag.getFieldCount());
		assertEquals(3, id3v23tag.getFields("PRIV").size());
		assertEquals(1, id3v23tag.getInvalidFrames()); // PRIV frame
		f.commit();
		f = (MP3File) AudioFileIO.read(testFile);
		tag = f.getTag();
		id3v23tag = (ID3v23Tag) tag;
		assertEquals(13, id3v23tag.getFieldCount());
		assertEquals(3, id3v23tag.getFields("PRIV").size());
		assertEquals(0, id3v23tag.getInvalidFrames()); // PRIV frame thrown away

	}
}
