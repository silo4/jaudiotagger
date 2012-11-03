package org.jaudiotagger.issues;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v11Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;

/**
 * Test deleting track total field shouldn't delete track field
 */
public class Issue420Test extends AbstractTestCase {
	public void testReadingFieldsThatOnlyExistInID3v1tag() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
		MP3File mp3File = new MP3File(testFile);
		assertFalse(mp3File.hasID3v1Tag());
		assertFalse(mp3File.hasID3v2Tag());
		mp3File.setID3v1Tag(new ID3v11Tag());
		mp3File.setID3v2Tag(new ID3v23Tag());
		mp3File.getID3v1Tag().setYear("1971");
		// TODO this seems wrong
		assertNull(mp3File.getTag());
		mp3File.save();
		mp3File = new MP3File(testFile);
		assertNotNull(mp3File.getTag());
		assertEquals(0, mp3File.getTag().getFields("TYER").size());
		assertEquals(0, mp3File.getTag().getFields(FieldKey.YEAR).size());
		assertEquals(1, mp3File.getID3v1Tag().getFields(FieldKey.YEAR).size());
	}
}
