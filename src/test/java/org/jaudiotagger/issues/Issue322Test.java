package org.jaudiotagger.issues;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Test tag Equality (specifically PartOfSet)
 */
public class Issue322Test extends AbstractTestCase {
	/*
	 * Test exception thrown
	 * 
	 * @throws Exception
	 */

	public void testNumberFieldHandling() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTag();
		Exception expected = null;
		try {
			tag.createField(FieldKey.TRACK_TOTAL, "");
		} catch (final Exception e) {
			expected = e;
		}

		assertNotNull(expected);
		assertTrue(expected instanceof FieldDataInvalidException);

		expected = null;
		try {
			tag.createField(FieldKey.TRACK_TOTAL, "1");
		} catch (final Exception e) {
			expected = e;
		}
		assertNull(expected);
	}
}
