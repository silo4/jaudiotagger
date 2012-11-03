package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.reference.Languages;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class Issue410Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("01.mp3");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("01.mp3");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, "English");
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("English", af.getTag().getFirst(FieldKey.LANGUAGE));

			af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, Languages.getInstanceOf().getIdForValue("English"));
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("eng", af.getTag().getFirst(FieldKey.LANGUAGE));
		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
