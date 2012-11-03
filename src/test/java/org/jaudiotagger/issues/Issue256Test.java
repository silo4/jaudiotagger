package org.jaudiotagger.issues;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Test Writing to new urls with common interface
 */
public class Issue256Test extends AbstractTestCase {
	/**
	 * Test Mp3
	 */
	public void testReadMp3() {

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test74.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			final Tag tag = af.getTag();
			final String value = tag.getFirst(FieldKey.TRACK);
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}
}
