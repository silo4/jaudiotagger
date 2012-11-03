package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Unable to save changes to file if backup .old file already exists
 */
public class Issue292Test extends AbstractTestCase {
	public void testSavingMp3File() {
		Path testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");
		if (!Files.isRegularFile(testFile)) {
			System.err.println("Unable to test file " + testFile.toAbsolutePath() + " - not available");
			return;
		}

		Path originalFileBackup = null;

		Exception exceptionCaught = null;
		try {

			testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");
			// Put file in backup location
			originalFileBackup = testFile.toAbsolutePath().getParent().resolve(AudioFile.getBaseFilename(testFile) + ".old");
			Files.move(testFile, originalFileBackup);

			// Copy over again
			testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

			// Read and save chnages
			AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(af.getTag().createField(FieldKey.ARTIST, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));
			af.getTag().setField(af.getTag().createField(FieldKey.AMAZON_ID, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));

			af.commit();

			af = AudioFileIO.read(testFile);
			assertEquals("fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", af.getTag().getFirst(FieldKey.ARTIST));
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		} finally {
			originalFileBackup.toFile().delete();
		}
		assertNull(exceptionCaught);
	}

	public void testSavingMp4File() {
		Path testFile = AbstractTestCase.copyAudioToTmp("test8.m4a");
		if (!Files.isRegularFile(testFile)) {
			System.err.println("Unable to test file " + testFile.toAbsolutePath() + " - not available");
			return;
		}

		Path originalFileBackup = null;

		Exception exceptionCaught = null;
		try {

			testFile = AbstractTestCase.copyAudioToTmp("test8.m4a");
			// Put file in backup location
			originalFileBackup = testFile.toAbsolutePath().getParent().resolve(AudioFile.getBaseFilename(testFile) + ".old");
			Files.move(testFile, originalFileBackup);

			// Copy over again
			testFile = AbstractTestCase.copyAudioToTmp("test8.m4a");

			// Read and save chnages
			AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(af.getTag().createField(FieldKey.ARTIST, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));
			af.getTag().setField(af.getTag().createField(FieldKey.AMAZON_ID, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));

			af.commit();

			af = AudioFileIO.read(testFile);
			assertEquals("fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", af.getTag().getFirst(FieldKey.ARTIST));
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		} finally {
			originalFileBackup.toFile().delete();
		}
		assertNull(exceptionCaught);
	}

}
