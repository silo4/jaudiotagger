package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Audio Books
 */
public class Issue283Test extends AbstractTestCase {

	public void testRead() {
		final Path orig = AbstractTestCase.dataPath.resolve("test56.m4b");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test56.m4b");
			final AudioFile af = AudioFileIO.read(testFile);
			System.out.println(af.getTag().toString());
			assertEquals("Aesop", af.getTag().getFirst(FieldKey.ARTIST));
			assertEquals("Aesop's Fables (Unabridged)", af.getTag().getFirst(FieldKey.TITLE));
			assertEquals("Aesop's Fables (Unabridged)", af.getTag().getFirst(FieldKey.ALBUM));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testWrite() {
		final Path orig = AbstractTestCase.dataPath.resolve("test56.m4b");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test56.m4b");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(FieldKey.ARTIST, "Aesops");
			af.commit();

			af = AudioFileIO.read(testFile);
			assertEquals("Aesops", af.getTag().getFirst(FieldKey.ARTIST));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
