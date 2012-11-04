package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.images.ArtworkFactory;

/**
 * Problem when relative filename has been specified
 */
public class Issue282Test extends AbstractTestCase {

	public void testWriteToRelativeWmaFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test1.wma");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test1.wma");

			// Copy up a level coz we need it to be in same folder as working directory so can just specify filename
			final Path outputFile = Paths.get(testFile.getFileName().toString());
			final boolean result = copy(testFile, outputFile);
			assertTrue(result);

			// make Relative
			assertTrue(Files.exists(outputFile));
			// Read File okay
			final AudioFile af = AudioFileIO.read(outputFile);
			System.out.println(af.getTag().toString());

			// Change File
			af.getTag().setField(ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.jpg")));

			af.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testWriteToRelativeMp3File() {
		final Path orig = AbstractTestCase.dataPath.resolve("testV1.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

			// Copy up a level coz we need it to be in same folder as working directory so can just specify filename
			final Path outputFile = Paths.get(testFile.getFileName().toString());
			final boolean result = copy(testFile, outputFile);
			assertTrue(result);

			// make Relative
			assertTrue(Files.exists(outputFile));
			// Read File okay
			final AudioFile af = AudioFileIO.read(outputFile);

			// Create tag and Change File
			af.getTagOrCreateAndSetDefault();
			af.getTag().setField(ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.jpg")));
			af.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
