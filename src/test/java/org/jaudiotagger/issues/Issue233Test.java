package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * Test Deleting v2 tags
 */
public class Issue233Test extends AbstractTestCase {
	public void testDeletingID3v2Tag() {
		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

			// No Tags
			MP3File mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertFalse(mp3File.hasID3v2Tag());

			// Save and deleteField v24 tag
			mp3File.setID3v2Tag(new ID3v24Tag());
			mp3File.save();
			mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertTrue(mp3File.hasID3v2Tag());

			mp3File.setID3v2Tag(null);
			mp3File.save();
			mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertFalse(mp3File.hasID3v2Tag());

			// Save and deleteField v23 tag
			mp3File.setID3v2Tag(new ID3v23Tag());
			mp3File.save();
			mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertTrue(mp3File.hasID3v2Tag());

			mp3File.setID3v2Tag(null);
			mp3File.save();
			mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertFalse(mp3File.hasID3v2Tag());

			// Save and deleteField v22 tag
			mp3File.setID3v2Tag(new ID3v22Tag());
			mp3File.save();
			mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertTrue(mp3File.hasID3v2Tag());

			mp3File.setID3v2Tag(null);
			mp3File.save();
			mp3File = new MP3File(testFile);
			assertFalse(mp3File.hasID3v1Tag());
			assertFalse(mp3File.hasID3v2Tag());

		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

	public void testDeletingID3v1Tag() {
		final Path orig = AbstractTestCase.dataPath.resolve("test32.mp3");
		if (!Files.isRegularFile(orig))
			return;

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test32.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			AudioFileIO.delete(af);
		} catch (final Exception e) {
			exceptionCaught = e;
			e.printStackTrace();
		}
		assertNull(exceptionCaught);
	}

	public void testReadingID3v1Tag() {
		final Path orig = AbstractTestCase.dataPath.resolve("test32.mp3");
		if (!Files.isRegularFile(orig))
			return;

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test32.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			final MP3File mf = (MP3File) af;
			assertEquals("The Ides Of March", af.getTag().getFirst(FieldKey.TITLE));
			assertEquals("Iron Maiden", mf.getID3v1Tag().getFirst(FieldKey.ARTIST));
			assertEquals("", mf.getID3v2Tag().getFirst(FieldKey.ARTIST));
			assertEquals("", af.getTag().getFirst(FieldKey.ARTIST));

		} catch (final Exception e) {
			exceptionCaught = e;
			e.printStackTrace();
		}
		assertNull(exceptionCaught);
	}
}
